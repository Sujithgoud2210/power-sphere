package com.powersphere.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotBlank(message = "Team name is required")
    private String name;

    @NotBlank(message = "Team code is required")
    private String code;

    private String description;
    private String teamLead;
}
