package com.vnpay.test.auth.service.authservice.controller;

import com.vnpay.test.auth.service.authservice.request.LoginRequest;
import com.vnpay.test.auth.service.authservice.request.RegisterRequest;
import com.vnpay.test.auth.service.authservice.request.ResetRequest;
import com.vnpay.test.auth.service.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping
    @PostMapping
    String getDescription() {
        return "This is Auth-Service";
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody Optional<LoginRequest> loginRequest) {
        if (loginRequest.isPresent()) {
            return authService.login(loginRequest.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Login fail!!!");
        }
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody Optional<RegisterRequest> registerRequest) {
        if (registerRequest.isPresent()) {
            return authService.register(registerRequest.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Register fail!!!");
        }
    }

    @PostMapping("/reset_refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody Optional<ResetRequest> resetRequest) {
        if (resetRequest.isPresent()) {
            return authService.resetRefreshToken(resetRequest.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Reset Access Token fail!!!");
        }
    }

    @PostMapping("/reset_access_token")
    public ResponseEntity<?> resetAccessToken(@RequestBody Optional<ResetRequest> refreshToken) {
        if (refreshToken.isPresent()) {
            return authService.resetAccessToken(refreshToken.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Reset Access Token fail!!!");
        }
    }

    @PostMapping("/validate_token")
    public ResponseEntity<?> validateToken(@RequestParam(name = "token", required = true) String accessToken) {
        return authService.validateAccessToken(accessToken);
    }

    @PostMapping("/log_out")
    public ResponseEntity<?> logOut(@RequestBody Optional<String> userId) {
        if (userId.isPresent()) {
            return authService.signOut(userId.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Reset Access Token fail!!!");
        }
    }
}
