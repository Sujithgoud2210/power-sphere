package com.powersphere.meter.mapper;

import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.dto.request.MeterUpdateRequest;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.entity.SmartMeter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeterMapper {

    @Mapping(target = "status", constant = "INACTIVE")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activationDate", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "barcode", ignore = true)
    @Mapping(target = "lastCommunicationTime", ignore = true)
    @Mapping(target = "lastMaintenanceDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    SmartMeter toEntity(MeterRegistrationRequest request);

    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.organizationName")
    @Mapping(target = "assignedUserId", source = "assignedUser.id")
    @Mapping(target = "assignedUserName", expression = "java(getAssignedUserName(meter))")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    MeterResponse toResponse(SmartMeter meter);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meterNumber", ignore = true)
    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "activationDate", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "barcode", ignore = true)
    @Mapping(target = "lastCommunicationTime", ignore = true)
    @Mapping(target = "lastMaintenanceDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(@MappingTarget SmartMeter meter, MeterUpdateRequest request);

    default String getAssignedUserName(SmartMeter meter) {
        if (meter.getAssignedUser() == null) {
            return null;
        }
        return meter.getAssignedUser().getFirstName() + " " + meter.getAssignedUser().getLastName();
    }
}
