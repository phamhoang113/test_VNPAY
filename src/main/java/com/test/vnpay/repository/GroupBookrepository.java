package com.test.vnpay.repository;

import com.test.vnpay.models.book.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupBookrepository  extends JpaRepository<BookGroup, Integer> {
    Optional<BookGroup> findByName(String name);
}
