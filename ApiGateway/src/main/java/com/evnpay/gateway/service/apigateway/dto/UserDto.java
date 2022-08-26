package com.evnpay.gateway.service.apigateway.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private String userId;
    private String userName;
    private Set<String> permission;
}
