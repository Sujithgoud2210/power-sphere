package com.powersphere.meter.service;

import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.repository.UserRepository;
import com.powersphere.meter.dto.request.AssignMeterRequest;
import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.dto.request.MeterUpdateRequest;
import com.powersphere.meter.dto.request.TransferMeterRequest;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import com.powersphere.meter.exception.DuplicateMeterException;
import com.powersphere.meter.exception.InvalidMeterStateException;
import com.powersphere.meter.exception.MeterAssignmentException;
import com.powersphere.meter.exception.MeterNotFoundException;
import com.powersphere.meter.mapper.MeterMapper;
import com.powersphere.meter.repository.SmartMeterRepository;
import com.powersphere.meter.service.impl.MeterServiceImpl;
import com.powersphere.meter.validation.MeterValidator;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeterServiceImplTest {

    @Mock
    private SmartMeterRepository smartMeterRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MeterMapper meterMapper;
    @Mock
    private MeterValidator meterValidator;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private MeterService meterService;

    private SmartMeter testMeter;
    private MeterRegistrationRequest registrationRequest;
    private MeterUpdateRequest updateRequest;
    private MeterResponse meterResponse;
    private UUID meterId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        meterService = new MeterServiceImpl(
                smartMeterRepository, organizationRepository, departmentRepository,
                userRepository, meterMapper, meterValidator, eventPublisher);

        meterId = UUID.randomUUID();
        userId = UUID.randomUUID();

        testMeter = SmartMeter.builder()
                .id(meterId)
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .status(MeterStatus.INACTIVE)
                .meterType(MeterType.SMART)
                .phaseType(PhaseType.THREE_PHASE)
                .connectionType(ConnectionType.COMMERCIAL)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        registrationRequest = MeterRegistrationRequest.builder()
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .meterType(MeterType.SMART)
                .phaseType(PhaseType.THREE_PHASE)
                .connectionType(ConnectionType.COMMERCIAL)
                .build();

        updateRequest = MeterUpdateRequest.builder()
                .manufacturer("Updated Manufacturer")
                .model("Updated Model")
                .build();

        meterResponse = MeterResponse.builder()
                .id(meterId)
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .status(MeterStatus.INACTIVE)
                .meterType(MeterType.SMART)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Register Meter")
    class RegisterMeter {

        @Test
        @DisplayName("Should register meter successfully")
        void shouldRegisterMeterSuccessfully() {
            when(meterMapper.toEntity(registrationRequest)).thenReturn(testMeter);
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            MeterResponse response = meterService.registerMeter(registrationRequest);

            assertThat(response).isNotNull();
            assertThat(response.getMeterNumber()).isEqualTo("MTR-2026-00001");
            verify(meterValidator).validateRegistration(registrationRequest);
            verify(smartMeterRepository, times(2)).save(any(SmartMeter.class));
            verify(eventPublisher).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw exception when meter number already exists")
        void shouldThrowExceptionWhenMeterNumberExists() {
            doThrow(new DuplicateMeterException("Meter number already exists"))
                    .when(meterValidator).validateRegistration(registrationRequest);

            assertThatThrownBy(() -> meterService.registerMeter(registrationRequest))
                    .isInstanceOf(DuplicateMeterException.class)
                    .hasMessageContaining("already exists");

            verify(smartMeterRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Meter")
    class GetMeter {

        @Test
        @DisplayName("Should get meter by ID")
        void shouldGetMeterById() {
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(meterMapper.toResponse(testMeter)).thenReturn(meterResponse);

            MeterResponse response = meterService.getMeterById(meterId);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(meterId);
        }

        @Test
        @DisplayName("Should throw exception when meter not found by ID")
        void shouldThrowExceptionWhenMeterNotFound() {
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> meterService.getMeterById(meterId))
                    .isInstanceOf(MeterNotFoundException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    @DisplayName("Update Meter")
    class UpdateMeter {

        @Test
        @DisplayName("Should update meter successfully")
        void shouldUpdateMeterSuccessfully() {
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            MeterResponse response = meterService.updateMeter(meterId, updateRequest);

            assertThat(response).isNotNull();
            verify(meterMapper).updateEntity(testMeter, updateRequest);
            verify(smartMeterRepository).save(testMeter);
        }
    }

    @Nested
    @DisplayName("Delete Meter")
    class DeleteMeter {

        @Test
        @DisplayName("Should soft delete meter successfully")
        void shouldSoftDeleteMeter() {
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);

            meterService.deleteMeter(meterId);

            assertThat(testMeter.getIsActive()).isFalse();
            assertThat(testMeter.getStatus()).isEqualTo(MeterStatus.REMOVED);
            verify(smartMeterRepository).save(testMeter);
        }
    }

    @Nested
    @DisplayName("Activate/Deactivate Meter")
    class ActivateDeactivateMeter {

        @Test
        @DisplayName("Should activate meter successfully")
        void shouldActivateMeter() {
            testMeter.setStatus(MeterStatus.INACTIVE);
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            meterService.activateMeter(meterId);

            assertThat(testMeter.getStatus()).isEqualTo(MeterStatus.ACTIVE);
            assertThat(testMeter.getActivationDate()).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception when activating already active meter")
        void shouldThrowExceptionWhenAlreadyActive() {
            testMeter.setStatus(MeterStatus.ACTIVE);
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));

            assertThatThrownBy(() -> meterService.activateMeter(meterId))
                    .isInstanceOf(InvalidMeterStateException.class)
                    .hasMessageContaining("already active");
        }

        @Test
        @DisplayName("Should deactivate meter successfully")
        void shouldDeactivateMeter() {
            testMeter.setStatus(MeterStatus.ACTIVE);
            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            meterService.deactivateMeter(meterId);

            assertThat(testMeter.getStatus()).isEqualTo(MeterStatus.INACTIVE);
        }
    }

    @Nested
    @DisplayName("Assign/Transfer Meter")
    class AssignTransferMeter {

        @Test
        @DisplayName("Should assign meter to user successfully")
        void shouldAssignMeter() {
            testMeter.setStatus(MeterStatus.ACTIVE);
            User user = User.builder().id(userId).firstName("John").lastName("Doe").build();
            AssignMeterRequest request = AssignMeterRequest.builder().userId(userId).build();

            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            meterService.assignMeter(meterId, request);

            assertThat(testMeter.getAssignedUser()).isEqualTo(user);
            verify(eventPublisher).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw exception when assigning inactive meter")
        void shouldThrowExceptionWhenAssigningInactiveMeter() {
            testMeter.setStatus(MeterStatus.INACTIVE);
            AssignMeterRequest request = AssignMeterRequest.builder().userId(userId).build();

            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));

            assertThatThrownBy(() -> meterService.assignMeter(meterId, request))
                    .isInstanceOf(InvalidMeterStateException.class);
        }

        @Test
        @DisplayName("Should transfer meter successfully")
        void shouldTransferMeter() {
            UUID oldUserId = UUID.randomUUID();
            UUID newUserId = UUID.randomUUID();
            User oldUser = User.builder().id(oldUserId).firstName("Old").lastName("User").build();
            User newUser = User.builder().id(newUserId).firstName("New").lastName("User").build();

            testMeter.setStatus(MeterStatus.ACTIVE);
            testMeter.setAssignedUser(oldUser);

            TransferMeterRequest request = TransferMeterRequest.builder()
                    .currentUserId(oldUserId)
                    .newUserId(newUserId)
                    .build();

            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));
            when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
            when(smartMeterRepository.save(any(SmartMeter.class))).thenReturn(testMeter);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            meterService.transferMeter(meterId, request);

            assertThat(testMeter.getAssignedUser()).isEqualTo(newUser);
            verify(eventPublisher).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw exception when transferring unassigned meter")
        void shouldThrowExceptionWhenTransferringUnassignedMeter() {
            testMeter.setAssignedUser(null);
            TransferMeterRequest request = TransferMeterRequest.builder()
                    .currentUserId(UUID.randomUUID())
                    .newUserId(UUID.randomUUID())
                    .build();

            when(smartMeterRepository.findById(meterId)).thenReturn(Optional.of(testMeter));

            assertThatThrownBy(() -> meterService.transferMeter(meterId, request))
                    .isInstanceOf(MeterAssignmentException.class)
                    .hasMessageContaining("not assigned");
        }
    }

    @Nested
    @DisplayName("Search/Filter Meters")
    class SearchFilterMeters {

        @Test
        @DisplayName("Should search meters by keyword")
        void shouldSearchMeters() {
            Page<SmartMeter> meterPage = new PageImpl<>(List.of(testMeter));
            when(smartMeterRepository.searchMeters(anyString(), any(Pageable.class)))
                    .thenReturn(meterPage);
            when(meterMapper.toResponse(any(SmartMeter.class))).thenReturn(meterResponse);

            Page<MeterResponse> result = meterService.searchMeters("Siemens", 0, 10, "createdAt", "DESC");

            assertThat(result).isNotEmpty();
            assertThat(result.getTotalElements()).isEqualTo(1);
        }
    }
}
