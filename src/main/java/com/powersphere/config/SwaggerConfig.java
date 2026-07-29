package com.powersphere.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Swagger/OpenAPI annotation-based configuration.
 * <p>
 * This configuration provides static OpenAPI metadata via annotations.
 * For dynamic configuration, see {@link OpenApiConfig}.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "PowerSphere API",
                version = "1.0.0-SNAPSHOT",
                description = "Enterprise Smart Energy Management Platform REST API",
                contact = @Contact(
                        name = "PowerSphere Team",
                        email = "dev@powersphere.com",
                        url = "https://powersphere.com"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development"),
                @Server(url = "https://api-dev.powersphere.com", description = "Staging"),
                @Server(url = "https://api.powersphere.com", description = "Production")
        },
        externalDocs = @ExternalDocumentation(
                description = "PowerSphere Wiki Documentation",
                url = "https://wiki.powersphere.com"
        )
)
public class SwaggerConfig {

    private SwaggerConfig() {
        throw new UnsupportedOperationException("Configuration class cannot be instantiated");
    }
}
