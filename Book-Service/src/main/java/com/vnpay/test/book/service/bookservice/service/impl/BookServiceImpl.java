package com.vnpay.test.book.service.bookservice.service.impl;

import com.vnpay.test.book.service.bookservice.entity.Book;
import com.vnpay.test.book.service.bookservice.entity.BookGroup;
import com.vnpay.test.book.service.bookservice.repository.BookRepository;
import com.vnpay.test.book.service.bookservice.repository.GroupBookRepository;
import com.vnpay.test.book.service.bookservice.request.InsertBookRequest;
import com.vnpay.test.book.service.bookservice.request.UpdateBookRequest;
import com.vnpay.test.book.service.bookservice.response.BaseResponse;
import com.vnpay.test.book.service.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private int pageSize = 10;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    GroupBookRepository groupBookRepository;

    @Override
    public ResponseEntity<?> getListBook(Optional<Integer> pageNumber, Optional<String> author) {
        int pageNum = 1;
        if(pageNumber.isPresent()){
            pageNum = pageNumber.get();
        }
        Page<Book> pageData;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, Sort.by("publishedDate").descending());
        if(author.isPresent()){
            pageData = bookRepository.findByAuthor(author.get(),pageable);
        }
        else{
            pageData = bookRepository.findAll(pageable);
        }
        return ResponseEntity.ok(pageData.get());
    }

    @Override
    public ResponseEntity<?> getBook(Optional<Long> bookId) {
        if(bookId.isPresent()){
            Book book = bookRepository.findById(bookId.get()).orElseThrow(
                    () -> new RuntimeException("Book not found!")
            );
            return ResponseEntity.ok(book);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error Book_id");
        }
    }

    @Override
    public ResponseEntity<?> insertBook(InsertBookRequest insertBookRequest) {
        try {
            Book book = new Book();
            book.setName(insertBookRequest.getBookName());
            book.setImage(insertBookRequest.getImage());
            book.setAuthor(insertBookRequest.getAuthor());
            book.setDescription(insertBookRequest.getDescription());
            Date publishedDate = new SimpleDateFormat(("ddMMyyyy")).parse(insertBookRequest.getPublishedDate().replaceAll("/","").replaceAll("-",""));
            book.setPublishedDate(publishedDate);
            book.setPrice(insertBookRequest.getPrice());
            book.setQuantity(insertBookRequest.getQuantity());
            book.setSaleOff(insertBookRequest.getSaleOff());
            Set<BookGroup> bookGroups = new HashSet<>();
            if(insertBookRequest.getBookGroup().size()>0){
                for(Integer i: insertBookRequest.getBookGroup()){
                    BookGroup bookGroup = groupBookRepository.findById(i).orElseThrow(
                            () -> new RuntimeException("Book Group not found!!!")
                    );
                    bookGroups.add(bookGroup);
                }
                book.setBookGroups(bookGroups);
            }
            else{
                book.setBookGroups(null);
            }
            bookRepository.save(book);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Insert book successfully!"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Insert Error!!!");
        }
    }

    @Override
    public ResponseEntity<?> updateBook(UpdateBookRequest updateBookRequest) {
        try{
            Book book = bookRepository.findById(updateBookRequest.getBookId()).orElseThrow(
                    () -> new RuntimeException("Book not found!")
            );
            book.setQuantity(updateBookRequest.getQuantity());
            book.setImage(updateBookRequest.getImage());
            book.setSaleOff(updateBookRequest.getSaleOff());
            book.setDescription(updateBookRequest.getDescription());
            bookRepository.save(book);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Update book successfully!"));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Update Error!!!");
        }
    }

    @Override
    public ResponseEntity<?> deleteBook(Optional<Long> bookId) {
        try{
            Book book = bookRepository.findById(bookId.get()).orElseThrow(
                    () -> new RuntimeException("Book not found!")
            );
            bookRepository.delete(book);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Delete book successfully!"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Delete Error!!!");
        }
    }
}
