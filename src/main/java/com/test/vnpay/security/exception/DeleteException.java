package com.test.vnpay.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DeleteException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DeleteException() {
        super("Error delete to DB!");
    }
}
