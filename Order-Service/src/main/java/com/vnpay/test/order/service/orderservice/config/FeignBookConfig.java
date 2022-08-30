package com.vnpay.test.order.service.orderservice.config;

import com.vnpay.test.order.service.orderservice.dto.BookDto;
import com.vnpay.test.order.service.orderservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class FeignBookConfig {
    @FeignClient("app.service.book-service.name")
    public interface  BookService{
        @GetMapping("/api/book/get_book/id/{book_id}")
        ResponseEntity<BookDto> getBookInfo(@PathVariable("book_id") String id);
    }
}
