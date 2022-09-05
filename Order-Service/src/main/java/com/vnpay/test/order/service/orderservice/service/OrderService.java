package com.vnpay.test.order.service.orderservice.service;

import com.vnpay.test.order.service.orderservice.request.CancelOrderRequest;
import com.vnpay.test.order.service.orderservice.request.InsertOrderRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface OrderService {
    ResponseEntity<?> getListOrder(String userId, Optional<Integer> pageNumber, HttpHeaders headers);
    ResponseEntity<?> insertOrder(String userId, InsertOrderRequest insertOrderRequest, HttpHeaders headers);
    ResponseEntity<?> cancelOrder(String userId, CancelOrderRequest cancelOrderRequest, HttpHeaders headers);
    ResponseEntity<?> getInfoOrder(String userId, Optional<Long> orderId, HttpHeaders headers);
}
