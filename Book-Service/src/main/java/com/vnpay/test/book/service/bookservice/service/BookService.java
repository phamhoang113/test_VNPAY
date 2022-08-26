package com.vnpay.test.book.service.bookservice.service;

import com.vnpay.test.book.service.bookservice.request.InsertBookRequest;
import com.vnpay.test.book.service.bookservice.request.UpdateBookRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BookService {
    ResponseEntity<?> getListBook(Optional<Integer> pageNumber, Optional<String> author);
    ResponseEntity<?> getBook(Optional<Long> bookId);
    ResponseEntity<?> insertBook(InsertBookRequest insertBookRequest);
    ResponseEntity<?> updateBook(UpdateBookRequest updateBookRequest);
    ResponseEntity<?> deleteBook(Optional<Long> bookId);
}
