package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.NotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import com.powersphere.notification.exception.NotificationTemplateNotFoundException;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.repository.NotificationTemplateRepository;
import com.powersphere.notification.service.NotificationTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link NotificationTemplateService} interface.
 * Manages the lifecycle of notification templates including creation,
 * updates, and querying by various criteria.
 */
@Service
@Transactional
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private static final Logger log = LoggerFactory.getLogger(NotificationTemplateServiceImpl.class);

    private final NotificationTemplateRepository templateRepository;
    private final NotificationMapper mapper;

    public NotificationTemplateServiceImpl(
            NotificationTemplateRepository templateRepository,
            NotificationMapper mapper) {
        this.templateRepository = templateRepository;
        this.mapper = mapper;
    }

    @Override
    public NotificationTemplateResponse createTemplate(NotificationTemplateRequest request) {
        if (templateRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException(
                    "Template with code '" + request.getCode() + "' already exists");
        }

        NotificationTemplate template = NotificationTemplate.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .channel(request.getChannel())
                .subjectTemplate(request.getSubjectTemplate())
                .bodyTemplate(request.getBodyTemplate())
                .active(request.isActive())
                .organizationId(request.getOrganizationId())
                .build();

        template = templateRepository.save(template);

        log.info("Notification template created: id={}, code={}", template.getId(), template.getCode());

        return mapper.toTemplateResponse(template);
    }

    @Override
    public NotificationTemplateResponse updateTemplate(Long id, NotificationTemplateRequest request) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new NotificationTemplateNotFoundException("id: " + id));

        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setType(request.getType());
        template.setChannel(request.getChannel());
        template.setSubjectTemplate(request.getSubjectTemplate());
        template.setBodyTemplate(request.getBodyTemplate());
        template.setActive(request.isActive());
        template.setOrganizationId(request.getOrganizationId());

        template = templateRepository.save(template);

        log.info("Notification template updated: id={}, code={}", template.getId(), template.getCode());

        return mapper.toTemplateResponse(template);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplate(Long id) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new NotificationTemplateNotFoundException("id: " + id));
        return mapper.toTemplateResponse(template);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateByCode(String code) {
        NotificationTemplate template = templateRepository.findByCode(code)
                .orElseThrow(() -> new NotificationTemplateNotFoundException(code));
        return mapper.toTemplateResponse(template);
    }

    @Override
    public void deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new NotificationTemplateNotFoundException("id: " + id);
        }
        templateRepository.deleteById(id);
        log.info("Notification template deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByTypeAndChannel(
            NotificationType type, NotificationChannel channel) {
        List<NotificationTemplate> templates = templateRepository
                .findByTypeAndChannelAndActiveTrue(type, channel);
        return mapper.toTemplateResponseList(templates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getAllActiveTemplates() {
        List<NotificationTemplate> templates = templateRepository.findByActiveTrue();
        return mapper.toTemplateResponseList(templates);
    }
}
