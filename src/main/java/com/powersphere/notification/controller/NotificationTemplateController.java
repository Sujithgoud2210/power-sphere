package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.NotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import com.powersphere.notification.service.NotificationTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing notification templates.
 * Templates define reusable notification content structures that can be
 * referenced by code when sending notifications.
 */
@RestController
@RequestMapping("/api/v1/notification-templates")
public class NotificationTemplateController {

    private static final Logger log = LoggerFactory.getLogger(NotificationTemplateController.class);

    private final NotificationTemplateService templateService;

    public NotificationTemplateController(NotificationTemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * Creates a new notification template.
     */
    @PostMapping
    public ResponseEntity<NotificationTemplateResponse> createTemplate(
            @Valid @RequestBody NotificationTemplateRequest request) {
        log.info("REST request to create template: code={}, name={}", request.getCode(), request.getName());
        NotificationTemplateResponse response = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates an existing notification template.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationTemplateResponse> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTemplateRequest request) {
        log.info("REST request to update template: id={}", id);
        NotificationTemplateResponse response = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a template by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationTemplateResponse> getTemplate(@PathVariable Long id) {
        log.debug("REST request to get template: id={}", id);
        NotificationTemplateResponse response = templateService.getTemplate(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a template by its unique code.
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<NotificationTemplateResponse> getTemplateByCode(@PathVariable String code) {
        log.debug("REST request to get template by code: code={}", code);
        NotificationTemplateResponse response = templateService.getTemplateByCode(code);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a template by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        log.info("REST request to delete template: id={}", id);
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all templates matching the given type and channel.
     */
    @GetMapping
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplatesByTypeAndChannel(
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) NotificationChannel channel) {
        if (type != null && channel != null) {
            log.debug("REST request to get templates by type={} and channel={}", type, channel);
            List<NotificationTemplateResponse> responses = templateService.getTemplatesByTypeAndChannel(type, channel);
            return ResponseEntity.ok(responses);
        }
        log.debug("REST request to get all active templates");
        List<NotificationTemplateResponse> responses = templateService.getAllActiveTemplates();
        return ResponseEntity.ok(responses);
    }
}
