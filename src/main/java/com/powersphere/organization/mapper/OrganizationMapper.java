package com.powersphere.organization.mapper;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationMapper {

    @Mapping(target = "isActive", constant = "true")
    Organization toEntity(OrganizationRequest request);

    OrganizationResponse toResponse(Organization organization);

    void updateEntity(@MappingTarget Organization organization, OrganizationRequest request);
}
