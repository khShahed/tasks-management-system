package com.shahed.taskmanagementsystem.dto.payload;

import com.shahed.taskmanagementsystem.jpa.entity.ERole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequest {
    @NotBlank(message = "New role is required")
    private ERole newRole;
}
