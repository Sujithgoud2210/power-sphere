package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.DuplicateDepartmentException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.DepartmentMapper;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import com.powersphere.organization.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 OrganizationRepository organizationRepository,
                                 DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        log.info("Creating department with code: {} in organization: {}", request.getCode(), request.getOrganizationId());

        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateDepartmentException(
                    "Department with code '" + request.getCode() + "' already exists");
        }

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found with id: " + request.getOrganizationId()));

        Department department = departmentMapper.toEntity(request);
        department.setOrganization(organization);

        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created with id: {}", savedDepartment.getId());

        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        log.info("Updating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));

        if (!department.getCode().equals(request.getCode())
                && departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateDepartmentException(
                    "Department with code '" + request.getCode() + "' already exists");
        }

        if (request.getOrganizationId() != null
                && (department.getOrganization() == null
                || !department.getOrganization().getId().equals(request.getOrganizationId()))) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException(
                            "Organization not found with id: " + request.getOrganizationId()));
            department.setOrganization(organization);
        }

        departmentMapper.updateEntity(department, request);
        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated with id: {}", updatedDepartment.getId());

        return departmentMapper.toResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        log.info("Deleting department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));

        department.setIsActive(false);
        departmentRepository.save(department);
        log.info("Department soft-deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {
        log.debug("Fetching department by id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + id));

        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByOrganization(UUID organizationId) {
        log.debug("Fetching departments for organization: {}", organizationId);

        return departmentRepository.findByOrganizationIdAndIsActiveTrue(organizationId).stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> searchDepartments(String name) {
        log.debug("Searching departments by name: {}", name);

        return departmentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        log.debug("Fetching all active departments");

        return departmentRepository.findByIsActiveTrue().stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
