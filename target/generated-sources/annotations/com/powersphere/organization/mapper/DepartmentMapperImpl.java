package com.powersphere.organization.mapper;

import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T17:58:33+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public Department toEntity(DepartmentRequest request) {
        if ( request == null ) {
            return null;
        }

        Department.DepartmentBuilder department = Department.builder();

        department.name( request.getName() );
        department.code( request.getCode() );
        department.description( request.getDescription() );
        department.manager( request.getManager() );

        department.isActive( true );

        return department.build();
    }

    @Override
    public DepartmentResponse toResponse(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentResponse.DepartmentResponseBuilder departmentResponse = DepartmentResponse.builder();

        departmentResponse.organizationId( departmentOrganizationId( department ) );
        departmentResponse.organizationName( departmentOrganizationOrganizationName( department ) );
        departmentResponse.id( department.getId() );
        departmentResponse.name( department.getName() );
        departmentResponse.code( department.getCode() );
        departmentResponse.description( department.getDescription() );
        departmentResponse.manager( department.getManager() );
        departmentResponse.isActive( department.getIsActive() );
        departmentResponse.createdAt( department.getCreatedAt() );
        departmentResponse.updatedAt( department.getUpdatedAt() );
        departmentResponse.createdBy( department.getCreatedBy() );
        departmentResponse.updatedBy( department.getUpdatedBy() );

        return departmentResponse.build();
    }

    @Override
    public void updateEntity(Department department, DepartmentRequest request) {
        if ( request == null ) {
            return;
        }

        department.setName( request.getName() );
        department.setCode( request.getCode() );
        department.setDescription( request.getDescription() );
        department.setManager( request.getManager() );
    }

    private UUID departmentOrganizationId(Department department) {
        Organization organization = department.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getId();
    }

    private String departmentOrganizationOrganizationName(Department department) {
        Organization organization = department.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getOrganizationName();
    }
}
