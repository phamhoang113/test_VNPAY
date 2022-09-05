package com.vnpay.test.auth.service.authservice.service;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    ResponseEntity<?> getUser(Optional<Long> userId, Optional<String> userName);
    boolean validateToken(String token);
}
