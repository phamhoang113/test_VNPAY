package com.vnpay.test.auth.service.authservice.response.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends BaseResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("role")
    private Set<String> roles;
}
