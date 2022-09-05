package com.vnpay.test.order.service.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    private String sdt;
    private String address;
    private int score;
}