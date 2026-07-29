package com.powersphere.organization.service;

import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentResponse createDepartment(UUID organizationId, DepartmentRequest request);

    DepartmentResponse updateDepartment(UUID id, DepartmentRequest request);

    void deleteDepartment(UUID id);

    DepartmentResponse getDepartmentById(UUID id);

    List<DepartmentResponse> getDepartmentsByOrganization(UUID organizationId);
}
