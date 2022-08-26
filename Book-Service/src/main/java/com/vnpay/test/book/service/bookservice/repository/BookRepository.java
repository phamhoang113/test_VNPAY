package com.vnpay.test.book.service.bookservice.repository;

import com.vnpay.test.book.service.bookservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);
    @Query("SELECT b from Book b where b.author like CONCAT(:author,'%')")
    Page<Book> findByAuthor(@Param("author")String author, Pageable pageable);
    @Query("SELECT count(b) from Book b where b.author like CONCAT(:author,'%')")
    int countAllByAuthor(@Param("author")String author);
}
