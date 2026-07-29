package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.DuplicateDepartmentException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.DepartmentMapper;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import com.powersphere.organization.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(UUID organizationId, DepartmentRequest request) {
        log.debug("Creating department with code: {} in organization: {}", request.getCode(), organizationId);

        var organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + organizationId));

        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateDepartmentException("Department with code '" + request.getCode() + "' already exists");
        }

        var department = departmentMapper.toEntity(request);
        department.setOrganization(organization);
        department = departmentRepository.save(department);
        log.info("Department created successfully with id: {}", department.getId());
        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        log.debug("Updating department with id: {}", id);

        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        departmentMapper.updateEntity(department, request);
        department = departmentRepository.save(department);
        log.info("Department updated successfully with id: {}", id);
        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        log.debug("Deleting department with id: {}", id);

        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        department.setIsActive(false);
        departmentRepository.save(department);
        log.info("Department soft-deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {
        log.debug("Fetching department by id: {}", id);

        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));

        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByOrganization(UUID organizationId) {
        log.debug("Fetching departments for organization: {}", organizationId);

        return departmentRepository.findByOrganizationId(organizationId)
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }
}
