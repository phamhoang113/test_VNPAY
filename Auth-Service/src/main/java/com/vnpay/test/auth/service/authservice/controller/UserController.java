package com.vnpay.test.auth.service.authservice.controller;

import com.vnpay.test.auth.service.authservice.repository.UserRepository;
import com.vnpay.test.auth.service.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private enum PERMISSION {READ, INSERT, UPDATE, DELETE};

    @Autowired
    UserService userService;

    @PostMapping(value = {"/get_user/id/{user_id}", "/get_user/name/{user_name}"})
    ResponseEntity<?> getUser(@RequestHeader HttpHeaders headers,
                              @PathVariable(name = "user_id") Optional<Long> userId,
                              @PathVariable(name = "user_name") Optional<String> userName){
        try {
            String permission = headers.get("x-user-permission").get(0);
            if(permission.contains(PERMISSION.READ.toString())){
                return userService.getUser(userId, userName);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Don't have permission to access this resource!");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Missing permission header!");
        }
    }
}
