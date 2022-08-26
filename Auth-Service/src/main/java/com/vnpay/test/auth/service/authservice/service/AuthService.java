package com.vnpay.test.auth.service.authservice.service;

import com.vnpay.test.auth.service.authservice.request.LoginRequest;
import com.vnpay.test.auth.service.authservice.request.RegisterRequest;
import com.vnpay.test.auth.service.authservice.request.ResetRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> register(RegisterRequest registerRequest);
    ResponseEntity<?> resetAccessToken(ResetRequest refreshToken);
    ResponseEntity<?> resetRefreshToken(ResetRequest refreshToken);
    ResponseEntity<?> validateAccessToken(String validateRequest);
    ResponseEntity<?> signOut(String userId);
}
