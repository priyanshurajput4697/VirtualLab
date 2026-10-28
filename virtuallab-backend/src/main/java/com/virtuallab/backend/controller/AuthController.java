
package com.virtuallab.backend.controller;

import com.virtuallab.backend.entity.User;
import com.virtuallab.backend.security.JwtService;
import com.virtuallab.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        try {
            String name = data.get("name");
            String email = data.get("email");
            String password = data.get("password");
            String roleText = data.getOrDefault("role", "STUDENT");

            if (name == null || email == null || password == null ||
                name.isBlank() || email.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "All fields are required"));
            }

            if (password.length() < 8) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password must be at least 8 characters"));
            }

            User.Role role = User.Role.valueOf(roleText.toUpperCase());
            User user = authService.register(name, email, password, role);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Registration successful",
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {
        try {
            String email = data.get("email");
            String password = data.get("password");

            if (email == null || password == null ||
                email.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email and password are required"));
            }

            User user = authService.login(email, password);
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
        }
    }
}
