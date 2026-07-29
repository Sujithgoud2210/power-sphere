package com.powersphere.meter.service;

import com.powersphere.meter.dto.request.*;
import com.powersphere.meter.dto.response.MeterResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MeterService {

    MeterResponse registerMeter(MeterRegistrationRequest request);

    MeterResponse updateMeter(UUID id, MeterUpdateRequest request);

    void deleteMeter(UUID id);

    MeterResponse getMeterById(UUID id);

    Page<MeterResponse> getAllMeters(int page, int size, String sortBy, String sortDirection);

    MeterResponse getMeterByMeterNumber(String meterNumber);

    MeterResponse getMeterBySerialNumber(String serialNumber);

    Page<MeterResponse> searchMeters(String searchTerm, int page, int size, String sortBy, String sortDirection);

    Page<MeterResponse> filterMeters(MeterSearchRequest searchRequest);

    MeterResponse activateMeter(UUID id);

    MeterResponse deactivateMeter(UUID id);

    MeterResponse assignMeter(UUID id, AssignMeterRequest request);

    MeterResponse transferMeter(UUID id, TransferMeterRequest request);
}
