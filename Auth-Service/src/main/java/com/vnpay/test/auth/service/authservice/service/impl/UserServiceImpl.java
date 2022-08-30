package com.vnpay.test.auth.service.authservice.service.impl;

import com.vnpay.test.auth.service.authservice.repository.UserRepository;
import com.vnpay.test.auth.service.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<?> getUser(Optional<Long> userId, Optional<String> userName) {
        try {
            if (userId.isPresent()) {
                return ResponseEntity.ok(userRepository.findById(userId.get()).get());
            } else {
                return ResponseEntity.ok(userRepository.findByUsername(userName.get()).get());
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
