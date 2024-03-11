package com.shahed.taskmanagementsystem.security.services;

import com.shahed.taskmanagementsystem.dto.payload.LoginRequest;
import com.shahed.taskmanagementsystem.dto.response.JwtResponse;
import com.shahed.taskmanagementsystem.jpa.entity.ERole;
import com.shahed.taskmanagementsystem.security.jwt.JwtUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class AuthenticatedUserService {
    private UserDetailsImpl userDetails;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public void loadAuthenticatedUser() {
        if(userDetails == null) {
            log.debug("Loading authenticated user");

            userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        }

        log.debug("Authenticated user: {}", userDetails.getUsername());
    }

    public boolean isAdministrator() {
        loadAuthenticatedUser();

        return userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_ADMIN.toString()));
    }

    public boolean isUser() {
        loadAuthenticatedUser();

        return userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_USER.toString()));
    }

    public String getUsername() {
        loadAuthenticatedUser();

        return userDetails.getUsername();
    }

    public ResponseEntity<JwtResponse> generateJwtToken(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
    }
}
