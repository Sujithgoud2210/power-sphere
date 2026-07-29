package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.CreateNotificationTemplateRequest;
import com.powersphere.notification.dto.request.UpdateNotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.exception.NotificationNotFoundException;
import com.powersphere.notification.mapper.NotificationTemplateMapper;
import com.powersphere.notification.repository.NotificationTemplateRepository;
import com.powersphere.notification.service.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplateMapper templateMapper;

    @Override
    public NotificationTemplateResponse createTemplate(CreateNotificationTemplateRequest request) {
        if (templateRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Template with code '" + request.getCode() + "' already exists");
        }

        NotificationTemplate template = templateMapper.toEntity(request);
        NotificationTemplate saved = templateRepository.save(template);
        log.info("Notification template created: id={}, code='{}'", saved.getId(), saved.getCode());
        return templateMapper.toResponse(saved);
    }

    @Override
    public NotificationTemplateResponse updateTemplate(Long id, UpdateNotificationTemplateRequest request) {
        NotificationTemplate template = findTemplateOrThrow(id);
        templateMapper.updateEntity(template, request);
        NotificationTemplate updated = templateRepository.save(template);
        log.info("Notification template updated: id={}", updated.getId());
        return templateMapper.toResponse(updated);
    }

    @Override
    public void deleteTemplate(Long id) {
        NotificationTemplate template = findTemplateOrThrow(id);
        templateRepository.delete(template);
        log.info("Notification template deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateById(Long id) {
        return templateMapper.toResponse(findTemplateOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateByCode(String code) {
        return templateRepository.findByCode(code)
                .map(templateMapper::toResponse)
                .orElseThrow(() -> new NotificationNotFoundException("Template not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getAllTemplates() {
        return templateRepository.findAll()
                .stream()
                .map(templateMapper::toResponse)
                .collect(Collectors.toList());
    }

    private NotificationTemplate findTemplateOrThrow(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification template not found with id: " + id));
    }
}
