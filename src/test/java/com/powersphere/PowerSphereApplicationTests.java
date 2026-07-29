package com.powersphere;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration tests for PowerSphere application context loading.
 * <p>
 * Verifies that the Spring Boot application context loads successfully
 * with all bean configurations. This is a foundational test that should
 * pass before any module-specific tests are written.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
class PowerSphereApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the application context starts successfully.
        // This test will fail if there are any bean creation issues,
        // missing dependencies, or configuration errors.
    }
}
