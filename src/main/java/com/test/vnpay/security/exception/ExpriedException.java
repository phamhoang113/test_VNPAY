package com.test.vnpay.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpriedException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ExpriedException() {
        super("Token is expried!");
    }
}