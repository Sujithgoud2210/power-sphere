package com.powersphere.billing.mapper;

import com.powersphere.billing.dto.response.BillHistoryResponse;
import com.powersphere.billing.dto.response.BillItemResponse;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.dto.response.PageResponse;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillHistory;
import com.powersphere.billing.entity.BillItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MapStruct mapper for converting between Bill entities and DTOs.
 * Delegates TariffPlan mapping to TariffPlanMapper.
 */
@Mapper(componentModel = "spring", uses = TariffPlanMapper.class)
public interface BillMapper {

    @Mapping(target = "tariffPlan", source = "tariffPlan")
    BillResponse toResponse(Bill bill);

    List<BillResponse> toResponseList(List<Bill> bills);

    BillItemResponse toItemResponse(BillItem billItem);

    List<BillItemResponse> toItemResponseList(List<BillItem> billItems);

    BillHistoryResponse toHistoryResponse(BillHistory billHistory);

    List<BillHistoryResponse> toHistoryResponseList(List<BillHistory> billHistories);

    default PageResponse<BillResponse> toPageResponse(Page<Bill> page) {
        List<BillResponse> content = toResponseList(page.getContent());
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
