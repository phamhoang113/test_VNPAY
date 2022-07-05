package com.vnpay.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AutoConfiguration
@SpringBootApplication
public class ManagerBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerBookApplication.class, args);
    }

}
