package com.vnpay.test.auth.service.authservice.response.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vnpay.test.auth.service.authservice.entity.User;
import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse extends BaseResponse {
    private long id;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    private String phone;
    private String address;
    private int score;

    public UserResponse(int code, String desc, User user) {
        super(code, desc);
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone = user.getSdt();
        this.score = user.getScore();
    }
}
