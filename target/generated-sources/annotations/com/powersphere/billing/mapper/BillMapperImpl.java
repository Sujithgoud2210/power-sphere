package com.powersphere.billing.mapper;

import com.powersphere.billing.dto.response.BillHistoryResponse;
import com.powersphere.billing.dto.response.BillItemResponse;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillHistory;
import com.powersphere.billing.entity.BillItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class BillMapperImpl implements BillMapper {

    @Autowired
    private TariffPlanMapper tariffPlanMapper;

    @Override
    public BillResponse toResponse(Bill bill) {
        if ( bill == null ) {
            return null;
        }

        BillResponse billResponse = new BillResponse();

        billResponse.setTariffPlan( tariffPlanMapper.toResponse( bill.getTariffPlan() ) );
        billResponse.setId( bill.getId() );
        billResponse.setBillNumber( bill.getBillNumber() );
        billResponse.setMeterId( bill.getMeterId() );
        billResponse.setMeterNumber( bill.getMeterNumber() );
        billResponse.setOrganizationId( bill.getOrganizationId() );
        billResponse.setOrganizationName( bill.getOrganizationName() );
        billResponse.setConsumerName( bill.getConsumerName() );
        billResponse.setConsumerAddress( bill.getConsumerAddress() );
        billResponse.setBillingMonth( bill.getBillingMonth() );
        billResponse.setBillingYear( bill.getBillingYear() );
        billResponse.setPreviousReading( bill.getPreviousReading() );
        billResponse.setCurrentReading( bill.getCurrentReading() );
        billResponse.setUnitsConsumed( bill.getUnitsConsumed() );
        billResponse.setEnergyCharge( bill.getEnergyCharge() );
        billResponse.setFixedCharge( bill.getFixedCharge() );
        billResponse.setServiceCharge( bill.getServiceCharge() );
        billResponse.setSubtotal( bill.getSubtotal() );
        billResponse.setTaxPercentage( bill.getTaxPercentage() );
        billResponse.setTaxAmount( bill.getTaxAmount() );
        billResponse.setDiscount( bill.getDiscount() );
        billResponse.setDiscountDescription( bill.getDiscountDescription() );
        billResponse.setLateFee( bill.getLateFee() );
        billResponse.setTotalAmount( bill.getTotalAmount() );
        billResponse.setAmountPaid( bill.getAmountPaid() );
        billResponse.setBalanceDue( bill.getBalanceDue() );
        billResponse.setStatus( bill.getStatus() );
        billResponse.setGeneratedDate( bill.getGeneratedDate() );
        billResponse.setDueDate( bill.getDueDate() );
        billResponse.setPaidDate( bill.getPaidDate() );
        billResponse.setRemarks( bill.getRemarks() );
        billResponse.setBillItems( toItemResponseList( bill.getBillItems() ) );
        billResponse.setBillHistories( toHistoryResponseList( bill.getBillHistories() ) );
        billResponse.setCreatedAt( bill.getCreatedAt() );
        billResponse.setUpdatedAt( bill.getUpdatedAt() );

        return billResponse;
    }

    @Override
    public List<BillResponse> toResponseList(List<Bill> bills) {
        if ( bills == null ) {
            return null;
        }

        List<BillResponse> list = new ArrayList<BillResponse>( bills.size() );
        for ( Bill bill : bills ) {
            list.add( toResponse( bill ) );
        }

        return list;
    }

    @Override
    public BillItemResponse toItemResponse(BillItem billItem) {
        if ( billItem == null ) {
            return null;
        }

        BillItemResponse billItemResponse = new BillItemResponse();

        billItemResponse.setId( billItem.getId() );
        billItemResponse.setItemType( billItem.getItemType() );
        billItemResponse.setDescription( billItem.getDescription() );
        billItemResponse.setQuantity( billItem.getQuantity() );
        billItemResponse.setRate( billItem.getRate() );
        billItemResponse.setAmount( billItem.getAmount() );
        billItemResponse.setSequence( billItem.getSequence() );

        return billItemResponse;
    }

    @Override
    public List<BillItemResponse> toItemResponseList(List<BillItem> billItems) {
        if ( billItems == null ) {
            return null;
        }

        List<BillItemResponse> list = new ArrayList<BillItemResponse>( billItems.size() );
        for ( BillItem billItem : billItems ) {
            list.add( toItemResponse( billItem ) );
        }

        return list;
    }

    @Override
    public BillHistoryResponse toHistoryResponse(BillHistory billHistory) {
        if ( billHistory == null ) {
            return null;
        }

        BillHistoryResponse billHistoryResponse = new BillHistoryResponse();

        billHistoryResponse.setId( billHistory.getId() );
        billHistoryResponse.setAction( billHistory.getAction() );
        billHistoryResponse.setPreviousStatus( billHistory.getPreviousStatus() );
        billHistoryResponse.setNewStatus( billHistory.getNewStatus() );
        billHistoryResponse.setChangedBy( billHistory.getChangedBy() );
        billHistoryResponse.setChangeDescription( billHistory.getChangeDescription() );
        billHistoryResponse.setChangedAt( billHistory.getChangedAt() );

        return billHistoryResponse;
    }

    @Override
    public List<BillHistoryResponse> toHistoryResponseList(List<BillHistory> billHistories) {
        if ( billHistories == null ) {
            return null;
        }

        List<BillHistoryResponse> list = new ArrayList<BillHistoryResponse>( billHistories.size() );
        for ( BillHistory billHistory : billHistories ) {
            list.add( toHistoryResponse( billHistory ) );
        }

        return list;
    }
}
