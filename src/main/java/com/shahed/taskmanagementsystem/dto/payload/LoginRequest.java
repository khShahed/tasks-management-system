package com.shahed.taskmanagementsystem.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Size(max = 20, message = "Username must be less than 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 120, message = "Password must be less than 120 characters")
    private String password;
}
