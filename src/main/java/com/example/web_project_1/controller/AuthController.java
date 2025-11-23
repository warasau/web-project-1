package com.example.web_project_1.controller;

import com.example.web_project_1.dto.LoginRequest;
import com.example.web_project_1.dto.RefreshRequest;
import com.example.web_project_1.dto.RegisterRequestDto;
import com.example.web_project_1.dto.TokenResponse;
import com.example.web_project_1.service.AuthService;
import com.example.web_project_1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            TokenResponse tokens = authService.login(request);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        try {
            TokenResponse tokens = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(tokens);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403 Reuse detected
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // 401 Invalid token
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto request,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error");
        }
        try {
            userService.registerUser(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}