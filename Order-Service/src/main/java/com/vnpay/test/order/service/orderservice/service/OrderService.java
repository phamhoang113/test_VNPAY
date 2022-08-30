package com.vnpay.test.order.service.orderservice.service;

import com.vnpay.test.order.service.orderservice.request.CancelOrderRequest;
import com.vnpay.test.order.service.orderservice.request.InsertOrderRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface OrderService {
    ResponseEntity<?> getListOrder(String userId, Optional<Integer> pageNumber);
    ResponseEntity<?> insertOrder(String userId, InsertOrderRequest insertOrderRequest);
    ResponseEntity<?> cancelOrder(String userId, CancelOrderRequest cancelOrderRequest);
    ResponseEntity<?> getInfoOrder(String userId, Optional<Long> orderId);
}
