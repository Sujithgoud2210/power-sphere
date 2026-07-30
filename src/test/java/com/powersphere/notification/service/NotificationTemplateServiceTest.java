package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.repository.NotificationTemplateRepository;
import com.powersphere.notification.service.impl.NotificationTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationTemplateServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class NotificationTemplateServiceTest {

    @Mock
    private NotificationTemplateRepository templateRepository;

    private NotificationMapper mapper;
    private NotificationTemplateServiceImpl templateService;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        templateService = new NotificationTemplateServiceImpl(templateRepository, mapper);
    }

    @Test
    void shouldCreateTemplateSuccessfully() {
        // Given
        NotificationTemplateRequest request = createValidRequest();
        NotificationTemplate savedTemplate = createTemplateEntity();

        when(templateRepository.existsByCode(request.getCode())).thenReturn(false);
        when(templateRepository.save(any(NotificationTemplate.class))).thenReturn(savedTemplate);

        // When
        NotificationTemplateResponse response = templateService.createTemplate(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("WELCOME_EMAIL");
        assertThat(response.getName()).isEqualTo("Welcome Email");
        assertThat(response.getType()).isEqualTo(NotificationType.ACCOUNT);
        assertThat(response.getChannel()).isEqualTo(NotificationChannel.EMAIL);

        verify(templateRepository, times(1)).existsByCode("WELCOME_EMAIL");
        verify(templateRepository, times(1)).save(any(NotificationTemplate.class));
    }

    @Test
    void shouldThrowExceptionWhenCodeAlreadyExists() {
        // Given
        NotificationTemplateRequest request = createValidRequest();

        when(templateRepository.existsByCode(request.getCode())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> templateService.createTemplate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(templateRepository, never()).save(any());
    }

    @Test
    void shouldGetTemplateByCode() {
        // Given
        String code = "WELCOME_EMAIL";
        NotificationTemplate template = createTemplateEntity();

        when(templateRepository.findByCode(code)).thenReturn(Optional.of(template));

        // When
        NotificationTemplateResponse response = templateService.getTemplateByCode(code);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(code);
    }

    @Test
    void shouldThrowExceptionWhenTemplateNotFound() {
        // Given
        String code = "NONEXISTENT";

        when(templateRepository.findByCode(code)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> templateService.getTemplateByCode(code))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    private NotificationTemplateRequest createValidRequest() {
        NotificationTemplateRequest request = new NotificationTemplateRequest();
        request.setCode("WELCOME_EMAIL");
        request.setName("Welcome Email");
        request.setDescription("Template for welcome emails");
        request.setType(NotificationType.ACCOUNT);
        request.setChannel(NotificationChannel.EMAIL);
        request.setSubjectTemplate("Welcome, {{name}}!");
        request.setBodyTemplate("Hello {{name}}, welcome to {{organization}}!");
        request.setActive(true);
        return request;
    }

    private NotificationTemplate createTemplateEntity() {
        return NotificationTemplate.builder()
                .id(1L)
                .code("WELCOME_EMAIL")
                .name("Welcome Email")
                .description("Template for welcome emails")
                .type(NotificationType.ACCOUNT)
                .channel(NotificationChannel.EMAIL)
                .subjectTemplate("Welcome, {{name}}!")
                .bodyTemplate("Hello {{name}}, welcome to {{organization}}!")
                .active(true)
                .build();
    }
}
