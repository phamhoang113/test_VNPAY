package com.vnpay.test.order.service.orderservice.config;

import com.vnpay.test.order.service.orderservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public class FeignUserConfig {
    @FeignClient("${app.service.user-service.name}")
    public interface  UserService{
        @GetMapping("/api/auth/get_user/id/{user_id}")
        ResponseEntity<UserDto> getUserInfo(@PathVariable("user_id") String id, @RequestHeader HttpHeaders headers);
    }
}
