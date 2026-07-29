package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.exception.DuplicateOrganizationException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.OrganizationMapper;
import com.powersphere.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationServiceImpl Unit Tests")
class OrganizationServiceImplTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMapper organizationMapper;

    private OrganizationServiceImpl organizationService;

    @Captor
    private ArgumentCaptor<Organization> organizationCaptor;

    private static final UUID ORG_ID = UUID.randomUUID();
    private static final String ORG_CODE = "ORG001";
    private static final String ORG_NAME = "Test Organization";

    private Organization organization;
    private OrganizationRequest request;
    private OrganizationResponse response;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationServiceImpl(organizationRepository, organizationMapper);

        organization = Organization.builder()
                .id(ORG_ID)
                .organizationCode(ORG_CODE)
                .organizationName(ORG_NAME)
                .status("ACTIVE")
                .isActive(true)
                .build();

        request = OrganizationRequest.builder()
                .organizationCode(ORG_CODE)
                .organizationName(ORG_NAME)
                .build();

        response = OrganizationResponse.builder()
                .id(ORG_ID)
                .organizationCode(ORG_CODE)
                .organizationName(ORG_NAME)
                .status("ACTIVE")
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Organization")
    class CreateOrganization {

        @Test
        @DisplayName("Should create organization successfully")
        void shouldCreateOrganizationSuccessfully() {
            when(organizationRepository.existsByOrganizationCode(ORG_CODE)).thenReturn(false);
            when(organizationMapper.toEntity(request)).thenReturn(organization);
            when(organizationRepository.save(any(Organization.class))).thenReturn(organization);
            when(organizationMapper.toResponse(organization)).thenReturn(response);

            OrganizationResponse result = organizationService.createOrganization(request);

            assertThat(result).isNotNull();
            assertThat(result.getOrganizationCode()).isEqualTo(ORG_CODE);
            assertThat(result.getOrganizationName()).isEqualTo(ORG_NAME);
            verify(organizationRepository).save(any(Organization.class));
        }

        @Test
        @DisplayName("Should throw exception when code already exists")
        void shouldThrowExceptionWhenCodeExists() {
            when(organizationRepository.existsByOrganizationCode(ORG_CODE)).thenReturn(true);

            assertThatThrownBy(() -> organizationService.createOrganization(request))
                    .isInstanceOf(DuplicateOrganizationException.class)
                    .hasMessageContaining(ORG_CODE);
        }
    }

    @Nested
    @DisplayName("Update Organization")
    class UpdateOrganization {

        @Test
        @DisplayName("Should update organization successfully")
        void shouldUpdateOrganizationSuccessfully() {
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.of(organization));
            when(organizationRepository.save(any(Organization.class))).thenReturn(organization);
            when(organizationMapper.toResponse(organization)).thenReturn(response);

            OrganizationResponse result = organizationService.updateOrganization(ORG_ID, request);

            assertThat(result).isNotNull();
            verify(organizationRepository).save(organization);
        }

        @Test
        @DisplayName("Should throw exception when organization not found")
        void shouldThrowExceptionWhenNotFound() {
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> organizationService.updateOrganization(ORG_ID, request))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Get Organization")
    class GetOrganization {

        @Test
        @DisplayName("Should get organization by ID")
        void shouldGetById() {
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.of(organization));
            when(organizationMapper.toResponse(organization)).thenReturn(response);

            OrganizationResponse result = organizationService.getOrganizationById(ORG_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(ORG_ID);
        }

        @Test
        @DisplayName("Should throw when organization not found")
        void shouldThrowWhenNotFound() {
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> organizationService.getOrganizationById(ORG_ID))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("List Organizations")
    class ListOrganizations {

        @Test
        @DisplayName("Should get all active organizations")
        void shouldGetAllActive() {
            when(organizationRepository.findByIsActiveTrue()).thenReturn(List.of(organization));
            when(organizationMapper.toResponse(organization)).thenReturn(response);

            var results = organizationService.getAllOrganizations();

            assertThat(results).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Organization")
    class DeleteOrganization {

        @Test
        @DisplayName("Should soft-delete organization")
        void shouldSoftDelete() {
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.of(organization));
            when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

            organizationService.deleteOrganization(ORG_ID);

            verify(organizationRepository).save(organizationCaptor.capture());
            assertThat(organizationCaptor.getValue().getIsActive()).isFalse();
        }
    }
}
