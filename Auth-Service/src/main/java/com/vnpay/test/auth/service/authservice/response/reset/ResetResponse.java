package com.vnpay.test.auth.service.authservice.response.reset;

import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetResponse extends BaseResponse {
    private String token;
    public ResetResponse(String token, String code, String desc){
        super(code, desc);
        this.token = token;
    }
}
