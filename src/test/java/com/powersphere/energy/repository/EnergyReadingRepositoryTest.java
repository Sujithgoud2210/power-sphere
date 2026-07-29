package com.powersphere.energy.repository;

import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EnergyReadingRepositoryTest {

    @Autowired
    private EnergyReadingRepository repository;

    private EnergyReading reading1;
    private EnergyReading reading2;
    private LocalDateTime timestamp1;
    private LocalDateTime timestamp2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        timestamp1 = LocalDateTime.of(2026, 7, 28, 10, 0, 0);
        timestamp2 = LocalDateTime.of(2026, 7, 29, 10, 0, 0);

        reading1 = repository.save(EnergyReading.builder()
                .meterId(1L)
                .readingTimestamp(timestamp1)
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .consumption(new BigDecimal("500.000"))
                .voltage(new BigDecimal("230.000"))
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build());

        reading2 = repository.save(EnergyReading.builder()
                .meterId(1L)
                .readingTimestamp(timestamp2)
                .readingType(ReadingType.SMART_METER)
                .previousReading(new BigDecimal("1500.000"))
                .currentReading(new BigDecimal("2000.000"))
                .consumption(new BigDecimal("500.000"))
                .voltage(new BigDecimal("240.000"))
                .readingSource(ReadingSource.DEVICE)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build());

        // An inactive reading (soft-deleted)
        repository.save(EnergyReading.builder()
                .meterId(1L)
                .readingTimestamp(timestamp1.minusDays(1))
                .readingType(ReadingType.MANUAL)
                .previousReading(new BigDecimal("500.000"))
                .currentReading(new BigDecimal("1000.000"))
                .consumption(new BigDecimal("500.000"))
                .readingSource(ReadingSource.MOBILE_APP)
                .qualityStatus(QualityStatus.VALID)
                .active(false)
                .build());

        // Another meter's reading
        repository.save(EnergyReading.builder()
                .meterId(2L)
                .readingTimestamp(timestamp2)
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("2000.000"))
                .currentReading(new BigDecimal("2500.000"))
                .consumption(new BigDecimal("500.000"))
                .readingSource(ReadingSource.API)
                .qualityStatus(QualityStatus.ESTIMATED)
                .active(true)
                .build());
    }

    @Test
    void shouldFindActiveReadingsWithPagination() {
        Page<EnergyReading> result = repository.findByActiveTrue(PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    void shouldFindByMeterIdAndActive() {
        Page<EnergyReading> result = repository.findByMeterIdAndActiveTrue(1L, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void shouldFindLatestReadingForMeter() {
        Optional<EnergyReading> latest = repository
                .findTopByMeterIdAndActiveTrueOrderByReadingTimestampDesc(1L);
        assertThat(latest).isPresent();
        assertThat(latest.get().getId()).isEqualTo(reading2.getId());
        assertThat(latest.get().getCurrentReading()).isEqualByComparingTo(new BigDecimal("2000.000"));
    }

    @Test
    void shouldFindReadingHistoryForMeter() {
        List<EnergyReading> history = repository
                .findByMeterIdAndActiveTrueOrderByReadingTimestampDesc(1L);
        assertThat(history).hasSize(2);
        assertThat(history.get(0).getId()).isEqualTo(reading2.getId()); // newest first
    }

    @Test
    void shouldDetectDuplicateReading() {
        boolean exists = repository.existsByMeterIdAndReadingTimestamp(
                1L, timestamp1);
        assertThat(exists).isTrue();
    }

    @Test
    void shouldNotDetectDuplicateForDifferentMeter() {
        boolean exists = repository.existsByMeterIdAndReadingTimestamp(
                2L, timestamp1);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldExcludeCurrentIdFromDuplicateCheck() {
        boolean exists = repository.existsByMeterIdAndReadingTimestampExcludingId(
                1L, timestamp1, reading1.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindReadingsByDateRange() {
        List<EnergyReading> readings = repository
                .findByMeterIdAndReadingTimestampBetweenAndActiveTrueOrderByReadingTimestampAsc(
                        1L,
                        timestamp1.minusHours(1),
                        timestamp2.plusHours(1));
        assertThat(readings).hasSize(2);
    }

    @Test
    void shouldCountReadingsByMeter() {
        long count = repository.countByMeterIdAndActiveTrue(1L);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldSupportSorting() {
        Page<EnergyReading> result = repository.findByActiveTrue(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "readingTimestamp")));
        List<EnergyReading> readings = result.getContent();
        assertThat(readings).hasSize(3);
        // First should be the oldest
        assertThat(readings.get(0).getId()).isNotEqualTo(reading2.getId());
    }
}
