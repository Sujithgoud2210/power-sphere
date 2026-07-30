package com.powersphere.meter.mapper;

import com.powersphere.authentication.entity.User;
import com.powersphere.meter.dto.request.MeterRegistrationRequest;
import com.powersphere.meter.dto.request.MeterUpdateRequest;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class MeterMapperImpl implements MeterMapper {

    @Override
    public SmartMeter toEntity(MeterRegistrationRequest request) {
        if ( request == null ) {
            return null;
        }

        SmartMeter.SmartMeterBuilder smartMeter = SmartMeter.builder();

        smartMeter.meterNumber( request.getMeterNumber() );
        smartMeter.serialNumber( request.getSerialNumber() );
        smartMeter.manufacturer( request.getManufacturer() );
        smartMeter.model( request.getModel() );
        smartMeter.firmwareVersion( request.getFirmwareVersion() );
        smartMeter.installationDate( request.getInstallationDate() );
        smartMeter.meterType( request.getMeterType() );
        smartMeter.phaseType( request.getPhaseType() );
        smartMeter.connectionType( request.getConnectionType() );
        smartMeter.voltage( request.getVoltage() );
        smartMeter.currentRating( request.getCurrentRating() );
        smartMeter.maxLoad( request.getMaxLoad() );
        smartMeter.latitude( request.getLatitude() );
        smartMeter.longitude( request.getLongitude() );
        smartMeter.installationAddress( request.getInstallationAddress() );
        smartMeter.city( request.getCity() );
        smartMeter.state( request.getState() );
        smartMeter.country( request.getCountry() );
        smartMeter.postalCode( request.getPostalCode() );
        smartMeter.remarks( request.getRemarks() );

        smartMeter.status( MeterStatus.INACTIVE );
        smartMeter.isActive( true );

        return smartMeter.build();
    }

    @Override
    public MeterResponse toResponse(SmartMeter meter) {
        if ( meter == null ) {
            return null;
        }

        MeterResponse.MeterResponseBuilder meterResponse = MeterResponse.builder();

        meterResponse.organizationId( meterOrganizationId( meter ) );
        meterResponse.organizationName( meterOrganizationOrganizationName( meter ) );
        meterResponse.assignedUserId( meterAssignedUserId( meter ) );
        meterResponse.departmentId( meterDepartmentId( meter ) );
        meterResponse.departmentName( meterDepartmentName( meter ) );
        meterResponse.id( meter.getId() );
        meterResponse.meterNumber( meter.getMeterNumber() );
        meterResponse.serialNumber( meter.getSerialNumber() );
        meterResponse.manufacturer( meter.getManufacturer() );
        meterResponse.model( meter.getModel() );
        meterResponse.firmwareVersion( meter.getFirmwareVersion() );
        meterResponse.installationDate( meter.getInstallationDate() );
        meterResponse.activationDate( meter.getActivationDate() );
        meterResponse.status( meter.getStatus() );
        meterResponse.meterType( meter.getMeterType() );
        meterResponse.phaseType( meter.getPhaseType() );
        meterResponse.connectionType( meter.getConnectionType() );
        meterResponse.voltage( meter.getVoltage() );
        meterResponse.currentRating( meter.getCurrentRating() );
        meterResponse.maxLoad( meter.getMaxLoad() );
        meterResponse.latitude( meter.getLatitude() );
        meterResponse.longitude( meter.getLongitude() );
        meterResponse.installationAddress( meter.getInstallationAddress() );
        meterResponse.city( meter.getCity() );
        meterResponse.state( meter.getState() );
        meterResponse.country( meter.getCountry() );
        meterResponse.postalCode( meter.getPostalCode() );
        meterResponse.qrCode( meter.getQrCode() );
        meterResponse.barcode( meter.getBarcode() );
        meterResponse.lastCommunicationTime( meter.getLastCommunicationTime() );
        meterResponse.lastMaintenanceDate( meter.getLastMaintenanceDate() );
        meterResponse.nextMaintenanceDate( meter.getNextMaintenanceDate() );
        meterResponse.remarks( meter.getRemarks() );
        meterResponse.isActive( meter.getIsActive() );
        meterResponse.createdAt( meter.getCreatedAt() );
        meterResponse.updatedAt( meter.getUpdatedAt() );
        meterResponse.createdBy( meter.getCreatedBy() );
        meterResponse.updatedBy( meter.getUpdatedBy() );

        meterResponse.assignedUserName( getAssignedUserName(meter) );

        return meterResponse.build();
    }

    @Override
    public void updateEntity(SmartMeter meter, MeterUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        meter.setManufacturer( request.getManufacturer() );
        meter.setModel( request.getModel() );
        meter.setFirmwareVersion( request.getFirmwareVersion() );
        meter.setInstallationDate( request.getInstallationDate() );
        meter.setMeterType( request.getMeterType() );
        meter.setPhaseType( request.getPhaseType() );
        meter.setConnectionType( request.getConnectionType() );
        meter.setVoltage( request.getVoltage() );
        meter.setCurrentRating( request.getCurrentRating() );
        meter.setMaxLoad( request.getMaxLoad() );
        meter.setLatitude( request.getLatitude() );
        meter.setLongitude( request.getLongitude() );
        meter.setInstallationAddress( request.getInstallationAddress() );
        meter.setCity( request.getCity() );
        meter.setState( request.getState() );
        meter.setCountry( request.getCountry() );
        meter.setPostalCode( request.getPostalCode() );
        meter.setNextMaintenanceDate( request.getNextMaintenanceDate() );
        meter.setRemarks( request.getRemarks() );
    }

    private UUID meterOrganizationId(SmartMeter smartMeter) {
        Organization organization = smartMeter.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getId();
    }

    private String meterOrganizationOrganizationName(SmartMeter smartMeter) {
        Organization organization = smartMeter.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getOrganizationName();
    }

    private UUID meterAssignedUserId(SmartMeter smartMeter) {
        User assignedUser = smartMeter.getAssignedUser();
        if ( assignedUser == null ) {
            return null;
        }
        return assignedUser.getId();
    }

    private UUID meterDepartmentId(SmartMeter smartMeter) {
        Department department = smartMeter.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getId();
    }

    private String meterDepartmentName(SmartMeter smartMeter) {
        Department department = smartMeter.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getName();
    }
}
