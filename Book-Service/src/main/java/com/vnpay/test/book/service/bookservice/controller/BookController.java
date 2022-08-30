package com.vnpay.test.book.service.bookservice.controller;

import com.vnpay.test.book.service.bookservice.request.InsertBookRequest;
import com.vnpay.test.book.service.bookservice.request.UpdateBookRequest;
import com.vnpay.test.book.service.bookservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/book")
public class BookController {
    final Logger logger = LoggerFactory.getLogger(BookController.class);
    private enum PERMISSION {READ, INSERT, UPDATE, DELETE};

    @GetMapping
    String info() {
        return "This is Book-Service";
    }

    @Autowired
    BookService bookService;

    @GetMapping(value = {"/list_book"})
    ResponseEntity<?> listBook(@RequestHeader HttpHeaders headers,
                               @RequestParam(name = "page_number") Optional<Integer> pageNumber,
                               @RequestParam(name = "author") Optional<String> author
                               ) {
        String permission = headers.get("x-user-permission").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.READ.toString())) {
            return bookService.getListBook(pageNumber, author);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @GetMapping(value = {"/get_book/id/{book_id}"})
    ResponseEntity<?> getBook(@RequestHeader HttpHeaders headers,
                               @PathVariable(name = "book_id") Optional<Long> bookId
    ) {
        String permission = headers.get("x-user-permission").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.READ.toString())) {
            return bookService.getBook(bookId);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = {"/insert_book"})
    ResponseEntity<?> insertBook(@RequestHeader HttpHeaders headers, @Valid @RequestBody InsertBookRequest insertBookRequest){
        String permission = headers.get("x-user-permission").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.INSERT.toString())) {
            return bookService.insertBook(insertBookRequest);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = {"/update_book"})
    ResponseEntity<?> updateBook(@RequestHeader HttpHeaders headers, @Valid @RequestBody UpdateBookRequest updateBookRequest){
        String permission = headers.get("x-user-permission").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.UPDATE.toString())) {
            return bookService.updateBook(updateBookRequest);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = "/delete_book/id/{book_id}")
    ResponseEntity<?> deleteBook(@RequestHeader HttpHeaders headers, @PathVariable(name = "book_id") Optional<Long> bookId){
        String permission = headers.get("x-user-permission").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.DELETE.toString())) {
            return bookService.deleteBook(bookId);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

}
