package com.test.vnpay.repository;

import com.test.vnpay.models.book.Book;
import com.test.vnpay.models.orders.OrdersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersBooksRepository extends JpaRepository<OrdersBooks, Long> {
}
