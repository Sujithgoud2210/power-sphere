package com.powersphere.meter.validation;

import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.exception.DuplicateMeterException;
import com.powersphere.meter.repository.SmartMeterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeterValidatorTest {

    @Mock
    private SmartMeterRepository smartMeterRepository;

    private MeterValidator meterValidator;

    @BeforeEach
    void setUp() {
        meterValidator = new MeterValidator(smartMeterRepository);
    }

    @Test
    @DisplayName("Should throw exception when meter number already exists")
    void shouldThrowExceptionWhenMeterNumberExists() {
        MeterRegistrationRequest request = MeterRegistrationRequest.builder()
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .meterType(MeterType.SMART)
                .connectionType(ConnectionType.COMMERCIAL)
                .build();

        when(smartMeterRepository.existsByMeterNumber("MTR-2026-00001")).thenReturn(true);

        assertThatThrownBy(() -> meterValidator.validateRegistration(request))
                .isInstanceOf(DuplicateMeterException.class)
                .hasMessageContaining("Meter with number");
    }

    @Test
    @DisplayName("Should throw exception when serial number already exists")
    void shouldThrowExceptionWhenSerialNumberExists() {
        MeterRegistrationRequest request = MeterRegistrationRequest.builder()
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .meterType(MeterType.SMART)
                .connectionType(ConnectionType.COMMERCIAL)
                .build();

        when(smartMeterRepository.existsByMeterNumber("MTR-2026-00001")).thenReturn(false);
        when(smartMeterRepository.existsBySerialNumber("SN-2026-XYZ-12345")).thenReturn(true);

        assertThatThrownBy(() -> meterValidator.validateRegistration(request))
                .isInstanceOf(DuplicateMeterException.class)
                .hasMessageContaining("serial number");
    }

    @Test
    @DisplayName("Should not throw when both are unique")
    void shouldNotThrowWhenUnique() {
        MeterRegistrationRequest request = MeterRegistrationRequest.builder()
                .meterNumber("MTR-2026-00001")
                .serialNumber("SN-2026-XYZ-12345")
                .manufacturer("Siemens")
                .model("SM-3000X")
                .meterType(MeterType.SMART)
                .connectionType(ConnectionType.COMMERCIAL)
                .build();

        when(smartMeterRepository.existsByMeterNumber("MTR-2026-00001")).thenReturn(false);
        when(smartMeterRepository.existsBySerialNumber("SN-2026-XYZ-12345")).thenReturn(false);

        // Should not throw
        meterValidator.validateRegistration(request);
    }
}
