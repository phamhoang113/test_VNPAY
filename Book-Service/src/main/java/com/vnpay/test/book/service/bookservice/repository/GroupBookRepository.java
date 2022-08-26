package com.vnpay.test.book.service.bookservice.repository;

import com.vnpay.test.book.service.bookservice.entity.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupBookRepository  extends JpaRepository<BookGroup, Integer> {
    Optional<BookGroup> findByName(String name);
}
