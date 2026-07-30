package com.powersphere.authentication.initializer;

import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.repository.RoleRepository;
import com.powersphere.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;
    private final String adminFirstName;
    private final String adminLastName;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.admin.email:admin@powersphere.com}") String adminEmail,
                           @Value("${app.admin.password:Admin@123}") String adminPassword,
                           @Value("${app.admin.first-name:System}") String adminFirstName,
                           @Value("${app.admin.last-name:Administrator}") String adminLastName) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("Database already initialized. Skipping data initialization.");
            return;
        }

        log.info("Starting database initialization...");

        createRoleIfNotFound("ADMIN", "System administrator with full access");
        createRoleIfNotFound("MANAGER", "Manager with operational access");
        createRoleIfNotFound("OPERATOR", "Operator with data entry access");
        createRoleIfNotFound("VIEWER", "Read-only access to dashboards and reports");

        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            User adminUser = User.builder()
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .email(adminEmail)
                    .username("admin")
                    .password(passwordEncoder.encode(adminPassword))
                    .enabled(true)
                    .accountLocked(false)
                    .emailVerified(true)
                    .status("ACTIVE")
                    .failedLoginAttempts(0)
                    .isActive(true)
                    .roles(Set.of(adminRole))
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(adminUser);
            log.info("Default admin user created successfully");
        }

        log.info("Database initialization completed successfully");
    }

    private void createRoleIfNotFound(String name, String description) {
        if (!roleRepository.existsByName(name)) {
            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .isActive(true)
                    .build();
            roleRepository.save(role);
            log.info("Role '{}' created", name);
        }
    }
}
