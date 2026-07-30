package com.powersphere.notification.util;

import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link NotificationTemplateEngine}.
 */
class NotificationTemplateEngineTest {

    private NotificationTemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        templateEngine = new NotificationTemplateEngine();
    }

    @Test
    void shouldRenderSimpleTemplate() {
        // Given
        String template = "Hello, {{name}}!";
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "John");

        // When
        String result = templateEngine.render(template, variables);

        // Then
        assertThat(result).isEqualTo("Hello, John!");
    }

    @Test
    void shouldRenderTemplateWithMultipleVariables() {
        // Given
        String template = "{{greeting}}, {{name}}! Welcome to {{organization}}.";
        Map<String, String> variables = new HashMap<>();
        variables.put("greeting", "Hello");
        variables.put("name", "Jane");
        variables.put("organization", "PowerSphere");

        // When
        String result = templateEngine.render(template, variables);

        // Then
        assertThat(result).isEqualTo("Hello, Jane! Welcome to PowerSphere.");
    }

    @Test
    void shouldLeaveUnresolvedVariablesIntact() {
        // Given
        String template = "Hello, {{name}}! Your code is {{code}}.";
        Map<String, String> variables = new HashMap<>();
        variables.put("name", "John");

        // When
        String result = templateEngine.render(template, variables);

        // Then
        assertThat(result).isEqualTo("Hello, John! Your code is {{code}}.");
    }

    @Test
    void shouldHandleNullTemplateGracefully() {
        // When
        String result = templateEngine.render(null, new HashMap<>());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleEmptyVariables() {
        // Given
        String template = "Hello, {{name}}!";

        // When
        String result = templateEngine.render(template, new HashMap<>());

        // Then
        assertThat(result).isEqualTo("Hello, {{name}}!");
    }

    @Test
    void shouldRenderFullNotificationTemplate() {
        // Given
        NotificationTemplate notificationTemplate = NotificationTemplate.builder()
                .code("WELCOME")
                .name("Welcome")
                .type(NotificationType.ACCOUNT)
                .channel(NotificationChannel.EMAIL)
                .subjectTemplate("Welcome, {{name}}!")
                .bodyTemplate("Hello {{name}},\n\nWelcome to {{organization}}!\n\nBest regards,\nThe Team")
                .build();

        Map<String, String> variables = new HashMap<>();
        variables.put("name", "Alice");
        variables.put("organization", "PowerSphere");

        // When
        NotificationTemplateEngine.RenderedContent rendered = templateEngine.renderTemplate(
                notificationTemplate, variables);

        // Then
        assertThat(rendered.getSubject()).isEqualTo("Welcome, Alice!");
        assertThat(rendered.getBody()).contains("Hello Alice");
        assertThat(rendered.getBody()).contains("Welcome to PowerSphere");
    }
}
