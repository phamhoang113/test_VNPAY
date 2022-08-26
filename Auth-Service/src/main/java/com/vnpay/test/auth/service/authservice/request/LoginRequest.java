package com.vnpay.test.auth.service.authservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("password")
    private String password;

}
