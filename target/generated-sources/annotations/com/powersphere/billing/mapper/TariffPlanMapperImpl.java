package com.powersphere.billing.mapper;

import com.powersphere.billing.dto.response.TariffPlanResponse;
import com.powersphere.billing.entity.TariffPlan;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class TariffPlanMapperImpl implements TariffPlanMapper {

    @Override
    public TariffPlanResponse toResponse(TariffPlan tariffPlan) {
        if ( tariffPlan == null ) {
            return null;
        }

        TariffPlanResponse tariffPlanResponse = new TariffPlanResponse();

        tariffPlanResponse.setId( tariffPlan.getId() );
        tariffPlanResponse.setPlanName( tariffPlan.getPlanName() );
        tariffPlanResponse.setPlanCode( tariffPlan.getPlanCode() );
        tariffPlanResponse.setConsumerType( tariffPlan.getConsumerType() );
        tariffPlanResponse.setFixedCharge( tariffPlan.getFixedCharge() );
        tariffPlanResponse.setEnergyChargePerUnit( tariffPlan.getEnergyChargePerUnit() );
        tariffPlanResponse.setTaxPercentage( tariffPlan.getTaxPercentage() );
        tariffPlanResponse.setServiceCharge( tariffPlan.getServiceCharge() );
        tariffPlanResponse.setEffectiveFrom( tariffPlan.getEffectiveFrom() );
        tariffPlanResponse.setEffectiveTo( tariffPlan.getEffectiveTo() );
        tariffPlanResponse.setActive( tariffPlan.isActive() );
        tariffPlanResponse.setCreatedAt( tariffPlan.getCreatedAt() );
        tariffPlanResponse.setUpdatedAt( tariffPlan.getUpdatedAt() );

        return tariffPlanResponse;
    }

    @Override
    public List<TariffPlanResponse> toResponseList(List<TariffPlan> tariffPlans) {
        if ( tariffPlans == null ) {
            return null;
        }

        List<TariffPlanResponse> list = new ArrayList<TariffPlanResponse>( tariffPlans.size() );
        for ( TariffPlan tariffPlan : tariffPlans ) {
            list.add( toResponse( tariffPlan ) );
        }

        return list;
    }
}
