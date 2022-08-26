package com.vnpay.test.auth.service.authservice.response.validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponse extends BaseResponse {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("permission")
    private Set<String> permissions;

    public ValidationResponse(String userName, String userId, Set<String> permissions, String code, String desc){
        super(code, desc);
        this.userName = userName;
        this.userId = userId;
        this.permissions = permissions;
    }
}
