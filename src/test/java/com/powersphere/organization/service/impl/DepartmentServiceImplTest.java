package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.DuplicateDepartmentException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.DepartmentMapper;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentServiceImpl Unit Tests")
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    private DepartmentServiceImpl departmentService;

    private static final UUID DEPT_ID = UUID.randomUUID();
    private static final UUID ORG_ID = UUID.randomUUID();
    private static final String DEPT_CODE = "DEPT001";
    private static final String DEPT_NAME = "Engineering";

    private Organization organization;
    private Department department;
    private DepartmentRequest request;
    private DepartmentResponse response;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(
                departmentRepository, organizationRepository, departmentMapper);

        organization = Organization.builder()
                .id(ORG_ID)
                .organizationCode("ORG001")
                .organizationName("Test Org")
                .build();

        department = Department.builder()
                .id(DEPT_ID)
                .code(DEPT_CODE)
                .name(DEPT_NAME)
                .organization(organization)
                .isActive(true)
                .build();

        request = DepartmentRequest.builder()
                .code(DEPT_CODE)
                .name(DEPT_NAME)
                .organizationId(ORG_ID)
                .build();

        response = DepartmentResponse.builder()
                .id(DEPT_ID)
                .code(DEPT_CODE)
                .name(DEPT_NAME)
                .organizationId(ORG_ID)
                .organizationName("Test Org")
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Department")
    class CreateDepartment {

        @Test
        @DisplayName("Should create department successfully")
        void shouldCreateSuccessfully() {
            when(departmentRepository.existsByCode(DEPT_CODE)).thenReturn(false);
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.of(organization));
            when(departmentMapper.toEntity(request)).thenReturn(department);
            when(departmentRepository.save(any(Department.class))).thenReturn(department);
            when(departmentMapper.toResponse(department)).thenReturn(response);

            var result = departmentService.createDepartment(request);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(DEPT_CODE);
        }

        @Test
        @DisplayName("Should throw when duplicate code")
        void shouldThrowWhenDuplicateCode() {
            when(departmentRepository.existsByCode(DEPT_CODE)).thenReturn(true);

            assertThatThrownBy(() -> departmentService.createDepartment(request))
                    .isInstanceOf(DuplicateDepartmentException.class);
        }

        @Test
        @DisplayName("Should throw when organization not found")
        void shouldThrowWhenOrgNotFound() {
            when(departmentRepository.existsByCode(DEPT_CODE)).thenReturn(false);
            when(organizationRepository.findById(ORG_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> departmentService.createDepartment(request))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Get Department")
    class GetDepartment {

        @Test
        @DisplayName("Should get by ID")
        void shouldGetById() {
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.of(department));
            when(departmentMapper.toResponse(department)).thenReturn(response);

            var result = departmentService.getDepartmentById(DEPT_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(DEPT_ID);
        }

        @Test
        @DisplayName("Should throw when not found")
        void shouldThrowWhenNotFound() {
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> departmentService.getDepartmentById(DEPT_ID))
                    .isInstanceOf(DepartmentNotFoundException.class);
        }
    }
}
