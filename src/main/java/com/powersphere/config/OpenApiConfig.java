package com.powersphere.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI powerSphereOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PowerSphere API")
                        .description("Enterprise Smart Energy Management Platform REST API")
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
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide a JWT access token. Example: Bearer <token>")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development"),
                        new Server()
                                .url("https://api-dev.powersphere.com")
                                .description("Staging"),
                        new Server()
                                .url("https://api.powersphere.com")
                                .description("Production")));
    }
}
