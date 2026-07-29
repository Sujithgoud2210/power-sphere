package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.entity.AlertRule;
import com.powersphere.notification.exception.AlertRuleNotFoundException;
import com.powersphere.notification.mapper.AlertRuleMapper;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.repository.AlertRuleRepository;
import com.powersphere.notification.service.impl.AlertRuleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertRuleServiceTest {

    @Mock
    private AlertRuleRepository alertRuleRepository;

    @Mock
    private AlertRuleMapper alertRuleMapper;

    private AlertRuleService alertRuleService;

    @BeforeEach
    void setUp() {
        alertRuleService = new AlertRuleServiceImpl(alertRuleRepository, alertRuleMapper);
    }

    @Test
    void createAlertRule_ShouldReturnResponse() {
        CreateAlertRuleRequest request = CreateAlertRuleRequest.builder()
                .name("High Usage Alert")
                .eventType("ENERGY_CONSUMPTION")
                .priority("HIGH")
                .build();

        AlertRule alertRule = AlertRule.builder()
                .id(1L)
                .name("High Usage Alert")
                .eventType("ENERGY_CONSUMPTION")
                .priority(NotificationPriority.HIGH)
                .isActive(true)
                .build();

        AlertRuleResponse response = AlertRuleResponse.builder()
                .id(1L)
                .name("High Usage Alert")
                .eventType("ENERGY_CONSUMPTION")
                .priority("HIGH")
                .isActive(true)
                .build();

        when(alertRuleMapper.toEntity(request)).thenReturn(alertRule);
        when(alertRuleRepository.save(any(AlertRule.class))).thenReturn(alertRule);
        when(alertRuleMapper.toResponse(alertRule)).thenReturn(response);

        AlertRuleResponse result = alertRuleService.createAlertRule(request);

        assertNotNull(result);
        assertEquals("High Usage Alert", result.getName());
        assertEquals("HIGH", result.getPriority());
    }

    @Test
    void getAlertRuleById_ShouldReturnResponse() {
        AlertRule alertRule = AlertRule.builder().id(1L).name("Test Rule").build();
        AlertRuleResponse response = AlertRuleResponse.builder().id(1L).name("Test Rule").build();

        when(alertRuleRepository.findById(1L)).thenReturn(Optional.of(alertRule));
        when(alertRuleMapper.toResponse(alertRule)).thenReturn(response);

        AlertRuleResponse result = alertRuleService.getAlertRuleById(1L);

        assertNotNull(result);
        assertEquals("Test Rule", result.getName());
    }

    @Test
    void getAlertRuleById_ShouldThrowException_WhenNotFound() {
        when(alertRuleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AlertRuleNotFoundException.class,
                () -> alertRuleService.getAlertRuleById(999L));
    }

    @Test
    void updateAlertRule_ShouldReturnUpdatedResponse() {
        UpdateAlertRuleRequest request = UpdateAlertRuleRequest.builder()
                .name("Updated Rule")
                .build();

        AlertRule alertRule = AlertRule.builder().id(1L).name("Original Rule").build();
        AlertRuleResponse response = AlertRuleResponse.builder().id(1L).name("Updated Rule").build();

        when(alertRuleRepository.findById(1L)).thenReturn(Optional.of(alertRule));
        when(alertRuleRepository.save(any(AlertRule.class))).thenReturn(alertRule);
        when(alertRuleMapper.toResponse(alertRule)).thenReturn(response);

        AlertRuleResponse result = alertRuleService.updateAlertRule(1L, request);

        assertNotNull(result);
        assertEquals("Updated Rule", result.getName());
    }

    @Test
    void deleteAlertRule_ShouldDeleteSuccessfully() {
        AlertRule alertRule = AlertRule.builder().id(1L).build();
        when(alertRuleRepository.findById(1L)).thenReturn(Optional.of(alertRule));

        alertRuleService.deleteAlertRule(1L);

        verify(alertRuleRepository).delete(alertRule);
    }

    @Test
    void toggleAlertRuleStatus_ShouldUpdateStatus() {
        AlertRule alertRule = AlertRule.builder()
                .id(1L)
                .isActive(false)
                .build();

        AlertRuleResponse response = AlertRuleResponse.builder()
                .id(1L)
                .isActive(true)
                .build();

        when(alertRuleRepository.findById(1L)).thenReturn(Optional.of(alertRule));
        when(alertRuleRepository.save(any(AlertRule.class))).thenReturn(alertRule);
        when(alertRuleMapper.toResponse(alertRule)).thenReturn(response);

        AlertRuleResponse result = alertRuleService.toggleAlertRuleStatus(1L, true);

        assertNotNull(result);
        assertTrue(result.getIsActive());
    }

    @Test
    void getAlertRulesByEventType_ShouldReturnList() {
        AlertRule alertRule = AlertRule.builder().id(1L).eventType("ENERGY").build();
        AlertRuleResponse response = AlertRuleResponse.builder().id(1L).eventType("ENERGY").build();

        when(alertRuleRepository.findByEventType("ENERGY")).thenReturn(List.of(alertRule));
        when(alertRuleMapper.toResponse(alertRule)).thenReturn(response);

        List<AlertRuleResponse> results = alertRuleService.getAlertRulesByEventType("ENERGY");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void getAllAlertRules_ShouldReturnPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertRule> page = new PageImpl<>(List.of(
                AlertRule.builder().id(1L).name("Rule 1").build()
        ));

        AlertRuleResponse response = AlertRuleResponse.builder().id(1L).name("Rule 1").build();

        when(alertRuleRepository.findAll(pageable)).thenReturn(page);
        when(alertRuleMapper.toResponse(any(AlertRule.class))).thenReturn(response);

        PagedResponse<AlertRuleResponse> result = alertRuleService.getAllAlertRules(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Rule 1", result.getContent().get(0).getName());
    }
}
