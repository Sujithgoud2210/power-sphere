package com.powersphere.billing.service.impl;

import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.entity.TariffPlan;
import com.powersphere.billing.enums.ConsumerType;
import com.powersphere.billing.exception.TariffPlanNotFoundException;
import com.powersphere.billing.mapper.TariffPlanMapper;
import com.powersphere.billing.repository.TariffPlanRepository;
import com.powersphere.billing.service.TariffPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the TariffPlanService interface.
 */
@Service
@Transactional
public class TariffPlanServiceImpl implements TariffPlanService {

    private static final Logger log = LoggerFactory.getLogger(TariffPlanServiceImpl.class);

    private final TariffPlanRepository tariffPlanRepository;
    private final TariffPlanMapper tariffPlanMapper;

    public TariffPlanServiceImpl(TariffPlanRepository tariffPlanRepository,
                                  TariffPlanMapper tariffPlanMapper) {
        this.tariffPlanRepository = tariffPlanRepository;
        this.tariffPlanMapper = tariffPlanMapper;
    }

    @Override
    public TariffPlanResponse createPlan(String planName, String planCode, ConsumerType consumerType,
                                          BigDecimal fixedCharge, BigDecimal energyChargePerUnit,
                                          BigDecimal taxPercentage, BigDecimal serviceCharge,
                                          LocalDate effectiveFrom, LocalDate effectiveTo) {
        if (tariffPlanRepository.existsByPlanCode(planCode)) {
            throw new IllegalArgumentException("Tariff plan with code '" + planCode + "' already exists");
        }

        TariffPlan plan = TariffPlan.builder()
                .planName(planName)
                .planCode(planCode)
                .consumerType(consumerType)
                .fixedCharge(fixedCharge)
                .energyChargePerUnit(energyChargePerUnit)
                .taxPercentage(taxPercentage)
                .serviceCharge(serviceCharge)
                .effectiveFrom(effectiveFrom)
                .effectiveTo(effectiveTo)
                .build();

        plan = tariffPlanRepository.save(plan);
        log.info("Tariff plan created: id={}, code={}, consumerType={}",
                plan.getId(), plan.getPlanCode(), plan.getConsumerType());

        return tariffPlanMapper.toResponse(plan);
    }

    @Override
    public TariffPlanResponse updatePlan(Long id, String planName,
                                          BigDecimal fixedCharge, BigDecimal energyChargePerUnit,
                                          BigDecimal taxPercentage, BigDecimal serviceCharge,
                                          LocalDate effectiveFrom, LocalDate effectiveTo,
                                          boolean active) {
        TariffPlan plan = tariffPlanRepository.findById(id)
                .orElseThrow(() -> new TariffPlanNotFoundException(id));

        plan.setPlanName(planName);
        plan.setFixedCharge(fixedCharge);
        plan.setEnergyChargePerUnit(energyChargePerUnit);
        plan.setTaxPercentage(taxPercentage);
        plan.setServiceCharge(serviceCharge);
        plan.setEffectiveFrom(effectiveFrom);
        plan.setEffectiveTo(effectiveTo);
        plan.setActive(active);

        plan = tariffPlanRepository.save(plan);
        log.info("Tariff plan updated: id={}, code={}", plan.getId(), plan.getPlanCode());

        return tariffPlanMapper.toResponse(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public TariffPlanResponse getPlan(Long id) {
        TariffPlan plan = tariffPlanRepository.findById(id)
                .orElseThrow(() -> new TariffPlanNotFoundException(id));
        return tariffPlanMapper.toResponse(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public TariffPlanResponse getPlanByCode(String code) {
        TariffPlan plan = tariffPlanRepository.findByPlanCode(code)
                .orElseThrow(() -> new TariffPlanNotFoundException(code));
        return tariffPlanMapper.toResponse(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TariffPlanResponse> getAllActivePlans() {
        List<TariffPlan> plans = tariffPlanRepository.findByActiveTrue();
        return tariffPlanMapper.toResponseList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TariffPlanResponse> getPlansByConsumerType(ConsumerType consumerType) {
        List<TariffPlan> plans = tariffPlanRepository.findByConsumerType(consumerType);
        return tariffPlanMapper.toResponseList(plans);
    }
}
