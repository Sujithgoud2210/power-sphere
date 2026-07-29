package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlertRuleService {

    AlertRuleResponse createAlertRule(CreateAlertRuleRequest request);

    AlertRuleResponse updateAlertRule(Long id, UpdateAlertRuleRequest request);

    void deleteAlertRule(Long id);

    AlertRuleResponse getAlertRuleById(Long id);

    PagedResponse<AlertRuleResponse> getAllAlertRules(Pageable pageable);

    List<AlertRuleResponse> getAlertRulesByEventType(String eventType);

    AlertRuleResponse toggleAlertRuleStatus(Long id, boolean isActive);
}
