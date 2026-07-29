package com.powersphere.billing.service.impl;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.dto.request.SearchBillRequest;
import com.powersphere.billing.dto.request.UpdateBillRequest;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.dto.response.PageResponse;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillHistory;
import com.powersphere.billing.entity.TariffPlan;
import com.powersphere.billing.enums.BillStatus;
import com.powersphere.billing.event.BillCancelledEvent;
import com.powersphere.billing.event.BillGeneratedEvent;
import com.powersphere.billing.event.BillUpdatedEvent;
import com.powersphere.billing.exception.BillNotFoundException;
import com.powersphere.billing.exception.TariffPlanNotFoundException;
import com.powersphere.billing.mapper.BillMapper;
import com.powersphere.billing.mapper.TariffPlanMapper;
import com.powersphere.billing.repository.BillRepository;
import com.powersphere.billing.repository.TariffPlanRepository;
import com.powersphere.billing.service.BillService;
import com.powersphere.billing.util.BillingCalculator;
import com.powersphere.billing.validation.BillValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the BillService interface. Handles the complete lifecycle
 * of electricity bills from generation through payment and cancellation.
 */
@Service
@Transactional
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);
    private static final String BILL_NUMBER_PREFIX = "PSP";
    private static final DateTimeFormatter BILL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final BillRepository billRepository;
    private final TariffPlanRepository tariffPlanRepository;
    private final BillMapper billMapper;
    private final BillValidator billValidator;
    private final BillingCalculator billingCalculator;
    private final ApplicationEventPublisher eventPublisher;

    private static final AtomicLong BILL_COUNTER = new AtomicLong(0);

    public BillServiceImpl(BillRepository billRepository,
                           TariffPlanRepository tariffPlanRepository,
                           BillMapper billMapper,
                           BillValidator billValidator,
                           BillingCalculator billingCalculator,
                           ApplicationEventPublisher eventPublisher) {
        this.billRepository = billRepository;
        this.tariffPlanRepository = tariffPlanRepository;
        this.billMapper = billMapper;
        this.billValidator = billValidator;
        this.billingCalculator = billingCalculator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BillResponse generateBill(GenerateBillRequest request) {
        // Validate request
        List<String> errors = billValidator.validateGenerateRequest(request);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Validation failed: " + String.join("; ", errors));
        }

        // Check if bill already exists for this meter and billing period
        long existingCount = billRepository.countByMeterIdAndBillingMonthAndBillingYear(
                request.getMeterId(), request.getBillingMonth(), request.getBillingYear());
        if (existingCount > 0) {
            throw new IllegalArgumentException(
                    "Bill already exists for meter " + request.getMeterId()
                    + " for period " + request.getBillingMonth() + "/" + request.getBillingYear());
        }

        // Resolve tariff plan
        TariffPlan tariffPlan = resolveTariffPlan(request);

        // Create and populate bill
        Bill bill = buildBillFromRequest(request, tariffPlan);

        // Calculate all charges
        billingCalculator.calculateBillCharges(bill, tariffPlan);

        // Generate unique bill number
        bill.setBillNumber(generateBillNumber());

        // Set status and dates
        bill.setStatus(BillStatus.GENERATED);
        bill.setGeneratedDate(LocalDate.now());
        if (request.getDueDate() == null) {
            bill.setDueDate(LocalDate.now().plusDays(30));
        }

        // Save bill
        bill = billRepository.save(bill);

        log.info("Bill generated: number={}, meterId={}, period={}/{}, units={}, amount={}",
                bill.getBillNumber(), bill.getMeterId(),
                bill.getBillingMonth(), bill.getBillingYear(),
                bill.getUnitsConsumed(), bill.getTotalAmount());

        // Publish event for audit history and notifications
        eventPublisher.publishEvent(new BillGeneratedEvent(this, bill));

        return billMapper.toResponse(bill);
    }

    @Override
    public BillResponse regenerateBill(Long id, GenerateBillRequest request) {
        // Cancel existing bill
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        String oldBillNumber = existingBill.getBillNumber();
        existingBill.setStatus(BillStatus.CANCELLED);
        billRepository.save(existingBill);

        eventPublisher.publishEvent(new BillCancelledEvent(this, existingBill,
                "Regenerated with updated readings"));

        // Generate new bill
        BillResponse newBill = generateBill(request);

        log.info("Bill regenerated: old={}, new={}", oldBillNumber, newBill.getBillNumber());

        return newBill;
    }

    @Override
    @Transactional(readOnly = true)
    public BillResponse getBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));
        return billMapper.toResponse(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public BillResponse getBillByNumber(String billNumber) {
        Bill bill = billRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new BillNotFoundException(billNumber));
        return billMapper.toResponse(bill);
    }

    @Override
    public BillResponse updateBill(Long id, UpdateBillRequest request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        StringBuilder changes = new StringBuilder();

        // Update remarks
        if (request.getRemarks() != null) {
            bill.setRemarks(request.getRemarks());
            changes.append("Remarks updated; ");
        }

        // Update discount
        if (request.getDiscount() != null) {
            bill.setDiscount(request.getDiscount());
            bill.setDiscountDescription(request.getDiscountDescription());
            changes.append("Discount updated; ");
        }

        // Update late fee
        if (request.getLateFee() != null) {
            bill.setLateFee(request.getLateFee());
            changes.append("Late fee updated; ");
        }

        // Recalculate total
        BigDecimal total = billingCalculator.calculateTotal(
                bill.getSubtotal(), bill.getTaxAmount(),
                bill.getDiscount(), bill.getLateFee());
        bill.setTotalAmount(total);
        bill.setBalanceDue(total.subtract(
                Optional.ofNullable(bill.getAmountPaid()).orElse(BigDecimal.ZERO)));

        bill = billRepository.save(bill);

        String changeDesc = changes.length() > 0 ? changes.toString() : "Bill updated";
        log.info("Bill updated: number={}, changes={}", bill.getBillNumber(), changeDesc);

        // Publish update event
        eventPublisher.publishEvent(new BillUpdatedEvent(this, bill, changeDesc));

        return billMapper.toResponse(bill);
    }

    @Override
    public BillResponse cancelBill(Long id, String reason) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));

        // Validate status transition
        billValidator.validateStatusTransition(bill, BillStatus.CANCELLED);

        bill.setStatus(BillStatus.CANCELLED);
        if (reason != null && !reason.isBlank()) {
            bill.setRemarks("CANCELLED: " + reason);
        }

        bill = billRepository.save(bill);

        log.info("Bill cancelled: number={}, reason={}", bill.getBillNumber(), reason);

        // Publish cancellation event
        eventPublisher.publishEvent(new BillCancelledEvent(this, bill, reason));

        return billMapper.toResponse(bill);
    }

    @Override
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new BillNotFoundException(id);
        }
        billRepository.deleteById(id);
        log.info("Bill deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BillResponse> searchBills(SearchBillRequest request) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(request.getSortDirection()),
                request.getSortBy());
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Bill> page = billRepository.searchBills(
                request.getMeterId(),
                request.getOrganizationId(),
                request.getStatus(),
                request.getBillingMonth(),
                request.getBillingYear(),
                request.getQuery(),
                pageable);

        return billMapper.toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BillResponse> getAllBills(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedDate"));
        Page<Bill> billPage = billRepository.findAll(pageable);
        return billMapper.toPageResponse(billPage);
    }

    /**
     * Resolves the applicable tariff plan from the request.
     */
    private TariffPlan resolveTariffPlan(GenerateBillRequest request) {
        if (request.getTariffPlanCode() != null && !request.getTariffPlanCode().isBlank()) {
            return tariffPlanRepository.findByPlanCode(request.getTariffPlanCode())
                    .orElseThrow(() -> new TariffPlanNotFoundException(request.getTariffPlanCode()));
        }
        // If no tariff code provided, find active tariff for the billing date
        LocalDate billingDate = LocalDate.of(request.getBillingYear(), request.getBillingMonth(), 1);
        return tariffPlanRepository.findActiveTariffForConsumer(
                        com.powersphere.billing.enums.ConsumerType.RESIDENTIAL, billingDate)
                .orElseThrow(() -> new TariffPlanNotFoundException(
                        "No active tariff plan found for period " + request.getBillingMonth()
                                + "/" + request.getBillingYear()));
    }

    /**
     * Builds a Bill entity from the generation request.
     */
    private Bill buildBillFromRequest(GenerateBillRequest request, TariffPlan tariffPlan) {
        return Bill.builder()
                .meterId(request.getMeterId())
                .meterNumber(request.getMeterNumber())
                .organizationId(request.getOrganizationId())
                .organizationName(request.getOrganizationName())
                .consumerName(request.getConsumerName())
                .consumerAddress(request.getConsumerAddress())
                .tariffPlan(tariffPlan)
                .billingMonth(request.getBillingMonth())
                .billingYear(request.getBillingYear())
                .previousReading(request.getPreviousReading())
                .currentReading(request.getCurrentReading())
                .remarks(request.getRemarks())
                .build();
    }

    /**
     * Generates a unique bill number using prefix, timestamp, and counter.
     */
    private synchronized String generateBillNumber() {
        long counter = BILL_COUNTER.incrementAndGet() % 10000;
        String timestamp = LocalDateTime.now().format(BILL_DATE_FORMAT);
        return BILL_NUMBER_PREFIX + timestamp + String.format("%04d", counter);
    }
}
