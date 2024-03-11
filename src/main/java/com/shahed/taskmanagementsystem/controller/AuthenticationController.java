package com.shahed.taskmanagementsystem.controller;

import com.shahed.taskmanagementsystem.dto.payload.LoginRequest;
import com.shahed.taskmanagementsystem.dto.payload.RegistrationRequest;
import com.shahed.taskmanagementsystem.dto.response.JwtResponse;
import com.shahed.taskmanagementsystem.dto.response.MessageResponse;
import com.shahed.taskmanagementsystem.security.services.AuthenticatedUserService;
import com.shahed.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticatedUserService authenticatedUserService;
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Operation(
        summary = "Login",
        description = "This endpoint allows a user to login to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show a JWT token"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid username or password"
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        log.debug("Login request: {}", loginRequest);

        return authenticatedUserService.generateJwtToken(loginRequest);
    }

    @Operation(
        summary = "Register",
        description = "This endpoint allows a user to register to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The response content will show a message"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Username is already taken or Email is already in use"
            )
        }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse> register(
        @Valid @RequestBody RegistrationRequest registrationRequest
    ) {
        log.debug("Register request: {}", registrationRequest);

        return userService.registration(registrationRequest, encoder.encode(registrationRequest.getPassword()));
    }
}
