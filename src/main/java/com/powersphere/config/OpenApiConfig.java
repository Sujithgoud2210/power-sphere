package com.powersphere.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) documentation configuration.
 * <p>
 * Configures the API documentation metadata including title, version,
 * contact information, and server URLs for different environments.
 */
@Configuration
public class OpenApiConfig {

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
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development"),
                        new Server().url("https://api-dev.powersphere.com").description("Staging"),
                        new Server().url("https://api.powersphere.com").description("Production")
                ));
    }
}
