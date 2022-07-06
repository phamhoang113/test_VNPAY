package com.test.vnpay.repository;

import com.test.vnpay.models.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookrRpository extends JpaRepository<Book, Long> {

}
