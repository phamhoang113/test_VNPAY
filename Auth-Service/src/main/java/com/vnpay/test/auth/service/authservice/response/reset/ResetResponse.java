package com.vnpay.test.auth.service.authservice.response.reset;

import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetResponse extends BaseResponse {
    private String token;
    public ResetResponse(int code, String desc, String token){
        super(code, desc);
        this.token = token;
    }
}
