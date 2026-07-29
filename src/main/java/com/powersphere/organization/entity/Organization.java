package com.powersphere.organization.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "organization_code", unique = true, nullable = false)
    private String organizationCode;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_number")
    private String taxNumber;

    private String industry;

    private String website;

    private String email;

    private String phone;

    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    private String city;

    private String state;

    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Builder.Default
    private String timezone = "UTC";

    @Builder.Default
    private String currency = "USD";

    @Column(name = "logo_url")
    private String logoUrl;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Department> departments = Set.of();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
