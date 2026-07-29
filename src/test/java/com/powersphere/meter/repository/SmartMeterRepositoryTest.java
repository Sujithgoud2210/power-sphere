package com.powersphere.meter.repository;

import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SmartMeterRepositoryTest {

    @Autowired
    private SmartMeterRepository smartMeterRepository;

    private SmartMeter testMeter;

    @BeforeEach
    void setUp() {
        testMeter = SmartMeter.builder()
                .meterNumber("MTR-TEST-00001")
                .serialNumber("SN-TEST-001")
                .manufacturer("Test Manufacturer")
                .model("Test Model")
                .status(MeterStatus.INACTIVE)
                .meterType(MeterType.SMART)
                .phaseType(PhaseType.THREE_PHASE)
                .connectionType(ConnectionType.COMMERCIAL)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        smartMeterRepository.save(testMeter);
    }

    @Test
    @DisplayName("Should find meter by meter number")
    void shouldFindByMeterNumber() {
        Optional<SmartMeter> found = smartMeterRepository.findByMeterNumber("MTR-TEST-00001");

        assertThat(found).isPresent();
        assertThat(found.get().getMeterNumber()).isEqualTo("MTR-TEST-00001");
    }

    @Test
    @DisplayName("Should find meter by serial number")
    void shouldFindBySerialNumber() {
        Optional<SmartMeter> found = smartMeterRepository.findBySerialNumber("SN-TEST-001");

        assertThat(found).isPresent();
        assertThat(found.get().getSerialNumber()).isEqualTo("SN-TEST-001");
    }

    @Test
    @DisplayName("Should check existence by meter number")
    void shouldCheckExistsByMeterNumber() {
        boolean exists = smartMeterRepository.existsByMeterNumber("MTR-TEST-00001");
        boolean notExists = smartMeterRepository.existsByMeterNumber("NONEXISTENT");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should check existence by serial number")
    void shouldCheckExistsBySerialNumber() {
        boolean exists = smartMeterRepository.existsBySerialNumber("SN-TEST-001");
        boolean notExists = smartMeterRepository.existsBySerialNumber("NONEXISTENT");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
