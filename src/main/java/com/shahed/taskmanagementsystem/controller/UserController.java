package com.shahed.taskmanagementsystem.controller;

import com.shahed.taskmanagementsystem.dto.payload.UpdateUserRoleRequest;
import com.shahed.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
        security = {
            @SecurityRequirement(name = "Bearer <Access Token>")
        },
        summary = "Change user role",
        description = "This endpoint allows an admin to change the role of a user.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show a message"
            )
        }
    )
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changeUserRole(
        @PathVariable("id") Long id,
        @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        userService.changeUserRole(id, request.getNewRole());

        return ResponseEntity.ok("User updated successfully!");
    }
}
