package com.powersphere.organization.mapper;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.entity.Organization;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T17:58:33+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public Organization toEntity(OrganizationRequest request) {
        if ( request == null ) {
            return null;
        }

        Organization.OrganizationBuilder organization = Organization.builder();

        organization.organizationCode( request.getOrganizationCode() );
        organization.organizationName( request.getOrganizationName() );
        organization.registrationNumber( request.getRegistrationNumber() );
        organization.taxNumber( request.getTaxNumber() );
        organization.industry( request.getIndustry() );
        organization.website( request.getWebsite() );
        organization.email( request.getEmail() );
        organization.phone( request.getPhone() );
        organization.status( request.getStatus() );
        organization.addressLine1( request.getAddressLine1() );
        organization.addressLine2( request.getAddressLine2() );
        organization.city( request.getCity() );
        organization.state( request.getState() );
        organization.country( request.getCountry() );
        organization.postalCode( request.getPostalCode() );
        organization.timezone( request.getTimezone() );
        organization.currency( request.getCurrency() );
        organization.logoUrl( request.getLogoUrl() );

        organization.isActive( true );

        return organization.build();
    }

    @Override
    public OrganizationResponse toResponse(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationResponse.OrganizationResponseBuilder organizationResponse = OrganizationResponse.builder();

        organizationResponse.id( organization.getId() );
        organizationResponse.organizationCode( organization.getOrganizationCode() );
        organizationResponse.organizationName( organization.getOrganizationName() );
        organizationResponse.registrationNumber( organization.getRegistrationNumber() );
        organizationResponse.taxNumber( organization.getTaxNumber() );
        organizationResponse.industry( organization.getIndustry() );
        organizationResponse.website( organization.getWebsite() );
        organizationResponse.email( organization.getEmail() );
        organizationResponse.phone( organization.getPhone() );
        organizationResponse.status( organization.getStatus() );
        organizationResponse.addressLine1( organization.getAddressLine1() );
        organizationResponse.addressLine2( organization.getAddressLine2() );
        organizationResponse.city( organization.getCity() );
        organizationResponse.state( organization.getState() );
        organizationResponse.country( organization.getCountry() );
        organizationResponse.postalCode( organization.getPostalCode() );
        organizationResponse.timezone( organization.getTimezone() );
        organizationResponse.currency( organization.getCurrency() );
        organizationResponse.logoUrl( organization.getLogoUrl() );
        organizationResponse.isActive( organization.getIsActive() );
        organizationResponse.createdAt( organization.getCreatedAt() );
        organizationResponse.updatedAt( organization.getUpdatedAt() );
        organizationResponse.createdBy( organization.getCreatedBy() );
        organizationResponse.updatedBy( organization.getUpdatedBy() );

        return organizationResponse.build();
    }

    @Override
    public void updateEntity(Organization organization, OrganizationRequest request) {
        if ( request == null ) {
            return;
        }

        organization.setOrganizationCode( request.getOrganizationCode() );
        organization.setOrganizationName( request.getOrganizationName() );
        organization.setRegistrationNumber( request.getRegistrationNumber() );
        organization.setTaxNumber( request.getTaxNumber() );
        organization.setIndustry( request.getIndustry() );
        organization.setWebsite( request.getWebsite() );
        organization.setEmail( request.getEmail() );
        organization.setPhone( request.getPhone() );
        organization.setStatus( request.getStatus() );
        organization.setAddressLine1( request.getAddressLine1() );
        organization.setAddressLine2( request.getAddressLine2() );
        organization.setCity( request.getCity() );
        organization.setState( request.getState() );
        organization.setCountry( request.getCountry() );
        organization.setPostalCode( request.getPostalCode() );
        organization.setTimezone( request.getTimezone() );
        organization.setCurrency( request.getCurrency() );
        organization.setLogoUrl( request.getLogoUrl() );
    }
}
