package com.powersphere.meter.validation;

import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.exception.DuplicateMeterException;
import com.powersphere.meter.repository.SmartMeterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeterValidator {

    private static final Logger log = LoggerFactory.getLogger(MeterValidator.class);

    private final SmartMeterRepository smartMeterRepository;

    /**
     * Validates meter registration request for uniqueness of meter number and serial number.
     */
    public void validateRegistration(MeterRegistrationRequest request) {
        log.debug("Validating meter registration for meter number: {}", request.getMeterNumber());

        if (smartMeterRepository.existsByMeterNumber(request.getMeterNumber())) {
            throw new DuplicateMeterException(
                    "Meter with number '" + request.getMeterNumber() + "' already exists");
        }

        if (smartMeterRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new DuplicateMeterException(
                    "Meter with serial number '" + request.getSerialNumber() + "' already exists");
        }
    }

    /**
     * Validates meter update request for uniqueness if fields are being changed.
     */
    public void validateUpdate(String meterNumber, String serialNumber) {
        if (meterNumber != null && smartMeterRepository.existsByMeterNumber(meterNumber)) {
            throw new DuplicateMeterException(
                    "Meter with number '" + meterNumber + "' already exists");
        }

        if (serialNumber != null && smartMeterRepository.existsBySerialNumber(serialNumber)) {
            throw new DuplicateMeterException(
                    "Meter with serial number '" + serialNumber + "' already exists");
        }
    }
}
