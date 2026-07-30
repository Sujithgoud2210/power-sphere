package com.powersphere.meter.mapper;

import com.powersphere.authentication.entity.User;
import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MeterMapperTest {

    private MeterMapper meterMapper;

    @BeforeEach
    void setUp() {
        meterMapper = Mappers.getMapper(MeterMapper.class);
    }

    @Test
    @DisplayName("Should map registration request to entity")
    void shouldMapRegistrationRequestToEntity() {
        MeterRegistrationRequest request = MeterRegistrationRequest.builder()
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .firmwareVersion("v2.1.4")
                .meterType(MeterType.SMART)
                .phaseType(PhaseType.THREE_PHASE)
                .connectionType(ConnectionType.COMMERCIAL)
                .voltage(new BigDecimal("240.00"))
                .currentRating(new BigDecimal("100.00"))
                .maxLoad(new BigDecimal("50.00"))
                .city("New York")
                .state("NY")
                .country("USA")
                .build();

        SmartMeter meter = meterMapper.toEntity(request);

        assertThat(meter).isNotNull();
        assertThat(meter.getMeterNumber()).isEqualTo("MTR-2026-00001");
        assertThat(meter.getSerialNumber()).isEqualTo("SN-2026-XYZ-12345");
        assertThat(meter.getManufacturer()).isEqualTo("Siemens");
        assertThat(meter.getModel()).isEqualTo("SM-3000X");
        assertThat(meter.getFirmwareVersion()).isEqualTo("v2.1.4");
        assertThat(meter.getMeterType()).isEqualTo(MeterType.SMART);
        assertThat(meter.getPhaseType()).isEqualTo(PhaseType.THREE_PHASE);
        assertThat(meter.getConnectionType()).isEqualTo(ConnectionType.COMMERCIAL);
        assertThat(meter.getVoltage()).isEqualByComparingTo(new BigDecimal("240.00"));
        assertThat(meter.getCurrentRating()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(meter.getMaxLoad()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(meter.getStatus()).isEqualTo(MeterStatus.INACTIVE);
        assertThat(meter.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("Should map entity to response")
    void shouldMapEntityToResponse() {
        UUID meterId = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Organization organization = Organization.builder()
                .id(orgId)
                .organizationName("Test Org")
                .build();

        Department department = Department.builder()
                .id(deptId)
                .name("Test Dept")
                .build();

        User user = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();

        SmartMeter meter = SmartMeter.builder()
                .id(meterId)
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .status(MeterStatus.ACTIVE)
                .meterType(MeterType.SMART)
                .phaseType(PhaseType.THREE_PHASE)
                .connectionType(ConnectionType.COMMERCIAL)
                .voltage(new BigDecimal("240.00"))
                .currentRating(new BigDecimal("100.00"))
                .maxLoad(new BigDecimal("50.00"))
                .city("New York")
                .state("NY")
                .country("USA")
                .isActive(true)
                .organization(organization)
                .department(department)
                .assignedUser(user)
                .createdAt(LocalDateTime.now())
                .build();

        MeterResponse response = meterMapper.toResponse(meter);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(meterId);
        assertThat(response.getMeterNumber()).isEqualTo("MTR-2026-00001");
        assertThat(response.getOrganizationId()).isEqualTo(orgId);
        assertThat(response.getOrganizationName()).isEqualTo("Test Org");
        assertThat(response.getDepartmentId()).isEqualTo(deptId);
        assertThat(response.getDepartmentName()).isEqualTo("Test Dept");
        assertThat(response.getAssignedUserId()).isEqualTo(userId);
        assertThat(response.getAssignedUserName()).isEqualTo("John Doe");
        assertThat(response.getStatus()).isEqualTo(MeterStatus.ACTIVE);
    }
}
