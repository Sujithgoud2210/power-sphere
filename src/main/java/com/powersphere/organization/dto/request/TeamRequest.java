package com.powersphere.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Team name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Team code is required")
    @Size(min = 2, max = 20, message = "Team code must be between {min} and {max} characters")
    private String code;

    @Size(max = 255, message = "Description must not exceed {max} characters")
    private String description;

    @Size(max = 100, message = "Team lead must not exceed {max} characters")
    private String teamLead;

    private UUID departmentId;
}
