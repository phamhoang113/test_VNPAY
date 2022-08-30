package com.vnpay.test.order.service.orderservice.repository;

import com.vnpay.test.order.service.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page findAll(Pageable pageable);
    Page<Order> findByUserId(long userId, Pageable pageable);
}
