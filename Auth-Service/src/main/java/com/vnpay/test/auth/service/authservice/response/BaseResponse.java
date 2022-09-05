package com.vnpay.test.auth.service.authservice.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private int code;
    private String desc;
}
