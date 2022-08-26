package com.vnpay.test.auth.service.authservice.service;

public interface JwtUtils {
    boolean validateToken(String token);
    String getUserNameFromToken(String token);
    String generateJwtTokenFromUserName(String userName);
}
