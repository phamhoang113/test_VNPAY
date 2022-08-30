package com.vnpay.test.order.service.orderservice.dto;

import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String sdt;
    private String address;
    private int score;
    private Set<Role> roles;
}

@Setter
@Getter
class Role{
    private int id;
    private String name;
}