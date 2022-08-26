package com.vnpay.test.order.service.orderservice.service.impl;

import com.vnpay.test.order.service.orderservice.repository.OrderRepository;
import com.vnpay.test.order.service.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
}
