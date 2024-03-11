package com.shahed.taskmanagementsystem.service;

import com.shahed.taskmanagementsystem.dto.payload.RegistrationRequest;
import com.shahed.taskmanagementsystem.dto.response.MessageResponse;
import com.shahed.taskmanagementsystem.jpa.entity.ERole;
import com.shahed.taskmanagementsystem.jpa.entity.User;
import com.shahed.taskmanagementsystem.jpa.repository.RoleRepository;
import com.shahed.taskmanagementsystem.jpa.repository.UserRepository;
import com.shahed.taskmanagementsystem.security.services.UserDetailsImpl;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);

        return userRepository.findByUsername(username);
    }

    public User getAuthenticatedUser() {
        log.debug("Getting authenticated user");

        return userRepository.findByUsername(getUsernameFromAuthentication())
            .orElse(null);
    }

    public ResponseEntity<MessageResponse> registration(
        RegistrationRequest registrationRequest,
        String encodedPassword
    ) {
        log.debug("Registering user: {}", registrationRequest.getUsername());

        if (Boolean.TRUE.equals(userRepository.existsByUsername(registrationRequest.getUsername()))) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(registrationRequest.getEmail()))) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(
            registrationRequest.getUsername(),
            registrationRequest.getEmail(),
            encodedPassword
        );

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public void changeUserRole(Long id, ERole newRole) {
        log.debug("Updating user roles: {}, new role: {}", id, newRole);

        var role = roleRepository.findByName(newRole)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        var user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        // Initialize roles set if it's null
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Add the new role to the existing roles set
        user.getRoles().clear();
        user.getRoles().add(role);

        userRepository.save(user);
    }

    private String getUsernameFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        }
        return null;
    }
}
