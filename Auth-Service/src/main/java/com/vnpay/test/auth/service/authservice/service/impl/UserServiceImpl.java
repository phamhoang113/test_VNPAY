package com.vnpay.test.auth.service.authservice.service.impl;

import com.vnpay.test.auth.service.authservice.entity.Role;
import com.vnpay.test.auth.service.authservice.entity.RoleEnum;
import com.vnpay.test.auth.service.authservice.entity.User;
import com.vnpay.test.auth.service.authservice.repository.UserRepository;
import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import com.vnpay.test.auth.service.authservice.response.user.UserResponse;
import com.vnpay.test.auth.service.authservice.response.validate.ValidationResponse;
import com.vnpay.test.auth.service.authservice.service.JwtUtils;
import com.vnpay.test.auth.service.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> getUser(Optional<Long> userId, Optional<String> userName) {
        try {
            if (userId.isPresent()) {
                return ResponseEntity.ok(new UserResponse(HttpStatus.OK.value(), "Successfully!", userRepository.findById(userId.get()).get()));
            } else {
                return ResponseEntity.ok(new UserResponse(HttpStatus.OK.value(), "Successfully!", userRepository.findByUsername(userName.get()).get()));
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Get user fail!"));
        }
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }
}
