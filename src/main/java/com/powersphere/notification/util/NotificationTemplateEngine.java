package com.powersphere.notification.util;

import com.powersphere.notification.entity.NotificationTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple template engine for rendering notification content from templates.
 * Supports variable substitution using {{variableName}} syntax within
 * template strings. Templates are resolved at send time with provided variables.
 */
@Component
public class NotificationTemplateEngine {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}");

    /**
     * Renders a template string by substituting variables with provided values.
     * Variables in the template are denoted by double curly braces: {{variableName}}.
     * If a variable is not found in the context, it remains unresolved in the output.
     *
     * @param template the template string containing {{variable}} placeholders
     * @param variables the map of variable names to values
     * @return the rendered string with variables substituted
     */
    public String render(String template, Map<String, String> variables) {
        if (template == null) {
            return "";
        }
        if (variables == null || variables.isEmpty()) {
            return template;
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement = variables.getOrDefault(variableName, matcher.group(0));
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Renders a notification template entity with the provided variables,
     * producing both the subject and body content.
     *
     * @param template the notification template entity
     * @param variables the variables to substitute
     * @return a {@link RenderedContent} containing the rendered subject and body
     */
    public RenderedContent renderTemplate(NotificationTemplate template, Map<String, String> variables) {
        String subject = render(template.getSubjectTemplate(), variables);
        String body = render(template.getBodyTemplate(), variables);
        return new RenderedContent(subject, body);
    }

    /**
     * Container for rendered template content.
     */
    public static class RenderedContent {
        private final String subject;
        private final String body;

        public RenderedContent(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }
}
