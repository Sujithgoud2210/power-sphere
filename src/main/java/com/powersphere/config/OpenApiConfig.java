package com.powersphere.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) documentation configuration.
 * <p>
 * Configures the API documentation metadata including title, version,
 * contact information, server URLs, security schemes, and API grouping tags.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI powerSphereOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PowerSphere API")
                        .description("""
                                Enterprise Smart Energy Management Platform REST API
                                
                                PowerSphere provides comprehensive energy management capabilities including:
                                * **Authentication & Authorization** - Secure JWT-based authentication with role-based access control
                                * **User Management** - Manage user profiles, roles, and organizational assignments
                                * **Organization Management** - Multi-level hierarchy: Organizations → Departments → Teams
                                * **Smart Meter Management** - Register, assign, transfer, and monitor smart meters
                                * **Energy Readings** - Record, track, and analyze energy consumption data
                                * **Billing & Tariffs** - Generate bills, manage tariff plans, and track payments
                                * **Notifications & Alerts** - Configurable alert rules and notification delivery
                                * **Dashboard & Analytics** - Real-time dashboards with consumption trends and revenue analytics
                                * **Reports** - Generate daily, weekly, monthly, yearly, and custom reports
                                """)
                        .version("1.0.0-SNAPSHOT")
                        .contact(new Contact()
                                .name("PowerSphere Team")
                                .email("dev@powersphere.com")
                                .url("https://powersphere.com"))
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        JWT Bearer Token Authentication.
                                        
                                        All protected endpoints require a valid JWT access token.
                                        Include the token in the Authorization header:
                                        
                                        ```
                                        Authorization: Bearer <your-access-token>
                                        ```
                                        
                                        To obtain a token:
                                        1. Call POST /api/v1/auth/login with valid credentials
                                        2. Use the accessToken from the response
                                        3. Tokens expire based on the configured expiration time
                                        4. Use POST /api/v1/auth/refresh-token to obtain a new access token
                                        """)))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api-dev.powersphere.com").description("Staging Environment"),
                        new Server().url("https://api.powersphere.com").description("Production Environment")
                ))
                .tags(List.of(
                        new Tag().name("Authentication").description("User registration, login, logout, token refresh, and password management endpoints").externalDocs(null),
                        new Tag().name("Users").description("User profile management, role assignment, and user search").externalDocs(null),
                        new Tag().name("Organizations").description("Organization CRUD operations and hierarchy management").externalDocs(null),
                        new Tag().name("Departments").description("Department management within organizations").externalDocs(null),
                        new Tag().name("Teams").description("Team management within departments").externalDocs(null),
                        new Tag().name("Smart Meter").description("Smart meter lifecycle: registration, assignment, transfer, activation, and monitoring").externalDocs(null),
                        new Tag().name("Energy Readings").description("Energy reading CRUD, consumption calculation, and historical data analysis").externalDocs(null),
                        new Tag().name("Billing").description("Bill generation, management, cancellation, and search").externalDocs(null),
                        new Tag().name("Tariff Plans").description("Tariff rate structure management for billing calculations").externalDocs(null),
                        new Tag().name("Notifications").description("Notification creation, scheduling, delivery, and management").externalDocs(null),
                        new Tag().name("Alert Rules").description("Configurable alert rule definitions for automated notifications").externalDocs(null),
                        new Tag().name("Notification Preferences").description("User notification channel and type preferences").externalDocs(null),
                        new Tag().name("Notification Templates").description("Notification content templates with variable substitution").externalDocs(null),
                        new Tag().name("Dashboard").description("Real-time dashboard with consumption trends, revenue analytics, and organizational comparisons").externalDocs(null),
                        new Tag().name("Reports").description("Generate daily, weekly, monthly, yearly, and custom analytical reports").externalDocs(null)
                ));
    }
}
