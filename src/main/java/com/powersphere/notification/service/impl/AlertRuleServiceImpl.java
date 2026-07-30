package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.entity.AlertRule;
import com.powersphere.notification.exception.AlertRuleNotFoundException;
import com.powersphere.notification.mapper.AlertRuleMapper;
import com.powersphere.notification.repository.AlertRuleRepository;
import com.powersphere.notification.service.AlertRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlertRuleServiceImpl implements AlertRuleService {

    private final AlertRuleRepository alertRuleRepository;
    private final AlertRuleMapper alertRuleMapper;

    @Override
    public AlertRuleResponse createAlertRule(CreateAlertRuleRequest request) {
        AlertRule alertRule = alertRuleMapper.toEntity(request);
        AlertRule saved = alertRuleRepository.save(alertRule);
        log.info("Alert rule created: id={}, name='{}'", saved.getId(), saved.getName());
        return alertRuleMapper.toResponse(saved);
    }

    @Override
    public AlertRuleResponse updateAlertRule(Long id, UpdateAlertRuleRequest request) {
        AlertRule alertRule = findAlertRuleOrThrow(id);
        alertRuleMapper.updateEntity(alertRule, request);
        AlertRule updated = alertRuleRepository.save(alertRule);
        log.info("Alert rule updated: id={}", updated.getId());
        return alertRuleMapper.toResponse(updated);
    }

    @Override
    public void deleteAlertRule(Long id) {
        AlertRule alertRule = findAlertRuleOrThrow(id);
        alertRuleRepository.delete(alertRule);
        log.info("Alert rule deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertRuleResponse getAlertRuleById(Long id) {
        return alertRuleMapper.toResponse(findAlertRuleOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AlertRuleResponse> getAllAlertRules(Pageable pageable) {
        Page<AlertRule> alertRulePage = alertRuleRepository.findAll(pageable);
        return buildPagedResponse(alertRulePage, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertRuleResponse> getAlertRulesByEventType(String eventType) {
        return alertRuleRepository.findByEventType(eventType)
                .stream()
                .map(alertRuleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlertRuleResponse toggleAlertRuleStatus(Long id, boolean isActive) {
        AlertRule alertRule = findAlertRuleOrThrow(id);
        alertRule.setIsActive(isActive);
        AlertRule saved = alertRuleRepository.save(alertRule);
        log.info("Alert rule status toggled: id={}, isActive={}", id, isActive);
        return alertRuleMapper.toResponse(saved);
    }

    private AlertRule findAlertRuleOrThrow(Long id) {
        return alertRuleRepository.findById(id)
                .orElseThrow(() -> new AlertRuleNotFoundException(id));
    }

    private PagedResponse<AlertRuleResponse> buildPagedResponse(Page<AlertRule> page, Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().stream().findFirst().orElse(null);
        return PagedResponse.<AlertRuleResponse>builder()
                .content(page.getContent().stream()
                        .map(alertRuleMapper::toResponse)
                        .collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .sortBy(sortOrder != null ? sortOrder.getProperty() : null)
                .sortDirection(sortOrder != null ? sortOrder.getDirection().name().toLowerCase() : null)
                .build();
    }
}
