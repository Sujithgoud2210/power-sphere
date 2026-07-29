package com.powersphere.energy.mapper;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import com.powersphere.energy.entity.EnergyReading;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T18:27:19+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class EnergyReadingMapperImpl implements EnergyReadingMapper {

    @Override
    public EnergyReading toEntity(EnergyReadingRequest request) {
        if ( request == null ) {
            return null;
        }

        EnergyReading energyReading = new EnergyReading();

        energyReading.setMeterId( request.getMeterId() );
        energyReading.setReadingTimestamp( request.getReadingTimestamp() );
        energyReading.setReadingType( request.getReadingType() );
        energyReading.setPreviousReading( request.getPreviousReading() );
        energyReading.setCurrentReading( request.getCurrentReading() );
        energyReading.setVoltage( request.getVoltage() );
        energyReading.setCurrent( request.getCurrent() );
        energyReading.setPowerFactor( request.getPowerFactor() );
        energyReading.setFrequency( request.getFrequency() );
        energyReading.setPower( request.getPower() );
        energyReading.setTemperature( request.getTemperature() );
        energyReading.setBatteryLevel( request.getBatteryLevel() );
        energyReading.setSignalStrength( request.getSignalStrength() );
        energyReading.setReadingSource( request.getReadingSource() );
        energyReading.setRemarks( request.getRemarks() );

        return energyReading;
    }

    @Override
    public EnergyReadingResponse toResponse(EnergyReading entity) {
        if ( entity == null ) {
            return null;
        }

        EnergyReadingResponse energyReadingResponse = new EnergyReadingResponse();

        energyReadingResponse.setMeterId( entity.getMeterId() );
        energyReadingResponse.setId( entity.getId() );
        energyReadingResponse.setReadingTimestamp( entity.getReadingTimestamp() );
        energyReadingResponse.setReadingType( entity.getReadingType() );
        energyReadingResponse.setPreviousReading( entity.getPreviousReading() );
        energyReadingResponse.setCurrentReading( entity.getCurrentReading() );
        energyReadingResponse.setConsumption( entity.getConsumption() );
        energyReadingResponse.setVoltage( entity.getVoltage() );
        energyReadingResponse.setCurrent( entity.getCurrent() );
        energyReadingResponse.setPowerFactor( entity.getPowerFactor() );
        energyReadingResponse.setFrequency( entity.getFrequency() );
        energyReadingResponse.setPower( entity.getPower() );
        energyReadingResponse.setTemperature( entity.getTemperature() );
        energyReadingResponse.setBatteryLevel( entity.getBatteryLevel() );
        energyReadingResponse.setSignalStrength( entity.getSignalStrength() );
        energyReadingResponse.setReadingSource( entity.getReadingSource() );
        energyReadingResponse.setQualityStatus( entity.getQualityStatus() );
        energyReadingResponse.setRemarks( entity.getRemarks() );
        energyReadingResponse.setActive( entity.isActive() );
        energyReadingResponse.setCreatedAt( entity.getCreatedAt() );
        energyReadingResponse.setUpdatedAt( entity.getUpdatedAt() );
        energyReadingResponse.setCreatedBy( entity.getCreatedBy() );
        energyReadingResponse.setUpdatedBy( entity.getUpdatedBy() );

        return energyReadingResponse;
    }

    @Override
    public ConsumptionResponse toConsumptionResponse(EnergyReading entity) {
        if ( entity == null ) {
            return null;
        }

        ConsumptionResponse consumptionResponse = new ConsumptionResponse();

        consumptionResponse.setMeterId( entity.getMeterId() );
        consumptionResponse.setPreviousReading( entity.getPreviousReading() );
        consumptionResponse.setCurrentReading( entity.getCurrentReading() );
        consumptionResponse.setConsumption( entity.getConsumption() );

        return consumptionResponse;
    }

    @Override
    public void updateEntity(EnergyReading entity, EnergyReadingRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setMeterId( request.getMeterId() );
        entity.setReadingTimestamp( request.getReadingTimestamp() );
        entity.setReadingType( request.getReadingType() );
        entity.setPreviousReading( request.getPreviousReading() );
        entity.setCurrentReading( request.getCurrentReading() );
        entity.setVoltage( request.getVoltage() );
        entity.setCurrent( request.getCurrent() );
        entity.setPowerFactor( request.getPowerFactor() );
        entity.setFrequency( request.getFrequency() );
        entity.setPower( request.getPower() );
        entity.setTemperature( request.getTemperature() );
        entity.setBatteryLevel( request.getBatteryLevel() );
        entity.setSignalStrength( request.getSignalStrength() );
        entity.setReadingSource( request.getReadingSource() );
        entity.setRemarks( request.getRemarks() );
    }
}
