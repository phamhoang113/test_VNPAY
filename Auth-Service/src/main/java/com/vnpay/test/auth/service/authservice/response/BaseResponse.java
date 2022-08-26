package com.vnpay.test.auth.service.authservice.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private String code;
    private String desc;
}
