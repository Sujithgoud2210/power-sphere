package com.powersphere.energy.mapper;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import com.powersphere.energy.entity.EnergyReading;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface EnergyReadingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consumption", ignore = true)
    @Mapping(target = "qualityStatus", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    EnergyReading toEntity(EnergyReadingRequest request);

    @Mapping(target = "meterId", source = "meterId")
    EnergyReadingResponse toResponse(EnergyReading entity);

    ConsumptionResponse toConsumptionResponse(EnergyReading entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consumption", ignore = true)
    @Mapping(target = "qualityStatus", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(@MappingTarget EnergyReading entity, EnergyReadingRequest request);
}
