package com.powersphere.billing.mapper;

import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.entity.TariffPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for converting between TariffPlan entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface TariffPlanMapper {

    TariffPlanResponse toResponse(TariffPlan tariffPlan);

    List<TariffPlanResponse> toResponseList(List<TariffPlan> tariffPlans);
}
