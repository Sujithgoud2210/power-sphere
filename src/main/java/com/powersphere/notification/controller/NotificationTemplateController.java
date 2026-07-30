package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.CreateNotificationTemplateRequest;
import com.powersphere.notification.dto.request.UpdateNotificationTemplateRequest;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.service.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification-templates")
@RequiredArgsConstructor
@Tag(name = "Notification Templates", description = "Notification content templates with variable substitution support")
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;

    @PostMapping
    @Operation(summary = "Create a notification template",
            description = "Creates a new notification template with variable placeholders for dynamic content. Templates support variables like {{userName}}, {{meterNumber}}, {{amount}}, etc.",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification template created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate template code")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> createTemplate(
            @Valid @RequestBody CreateNotificationTemplateRequest request) {
        NotificationTemplateResponse response = templateService.createTemplate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification template created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a notification template",
            description = "Updates an existing notification template's content, subject, or variables. Referenced templates in active alert rules will use the updated content.",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification template updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification template not found")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> updateTemplate(
            @Parameter(description = "Template ID", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationTemplateRequest request) {
        NotificationTemplateResponse response = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification template updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification template",
            description = "Deletes a notification template. Alert rules referencing this template will no longer use it for generating notification content.",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification template deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification template not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(
            @Parameter(description = "Template ID", example = "1", required = true) @PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Notification template deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get template by ID",
            description = "Retrieves a notification template's content, variables, and metadata by its unique identifier.",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Template retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getTemplateById(
            @Parameter(description = "Template ID", example = "1", required = true) @PathVariable Long id) {
        NotificationTemplateResponse response = templateService.getTemplateById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get template by code",
            description = "Retrieves a notification template using its unique code identifier (e.g., BILL_GENERATED, HIGH_CONSUMPTION_ALERT).",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Template retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<ApiResponse<NotificationTemplateResponse>> getTemplateByCode(
            @Parameter(description = "Unique template code", example = "BILL_GENERATED", required = true) @PathVariable String code) {
        NotificationTemplateResponse response = templateService.getTemplateByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all templates",
            description = "Retrieves a list of all available notification templates with their content, variables, and metadata.",
            tags = {"Notification Templates"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Templates retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponse>>> getAllTemplates() {
        List<NotificationTemplateResponse> response = templateService.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
