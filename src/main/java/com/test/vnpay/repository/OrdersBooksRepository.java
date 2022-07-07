package com.test.vnpay.repository;

import com.test.vnpay.models.orders.OrdersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersBooksRepository extends JpaRepository<OrdersBooks, Long> {
}
