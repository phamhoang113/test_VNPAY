package com.vnpay.test.book.service.bookservice.service.impl;

import com.vnpay.test.book.service.bookservice.entity.Book;
import com.vnpay.test.book.service.bookservice.entity.BookGroup;
import com.vnpay.test.book.service.bookservice.repository.BookRepository;
import com.vnpay.test.book.service.bookservice.repository.GroupBookRepository;
import com.vnpay.test.book.service.bookservice.request.InsertBookRequest;
import com.vnpay.test.book.service.bookservice.request.UpdateBookRequest;
import com.vnpay.test.book.service.bookservice.response.BaseResponse;
import com.vnpay.test.book.service.bookservice.response.BookResponse;
import com.vnpay.test.book.service.bookservice.response.ListBookResponse;
import com.vnpay.test.book.service.bookservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private int pageSize = 10;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    GroupBookRepository groupBookRepository;

    @Override
    public ResponseEntity<?> getListBook(Optional<Integer> pageNumber, Optional<String> author) {
        try{
            log.info("Get list books with page number :{},author:{}",pageNumber,author);
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
            long countAll = bookRepository.count();
            log.info("Get list books successfully!");
            return ResponseEntity.ok(new ListBookResponse(HttpStatus.OK.value(), "Successfully!",countAll, pageData));
        }
        catch (Exception e){
            log.error("Error get list books"+e);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Books is empty"));
        }

    }

    @Override
    public ResponseEntity<?> getBook(Optional<Long> bookId) {
        try {
            log.info("Get book detail with book id:{}", bookId);
            if (bookId.isPresent()) {
                Book book = bookRepository.findById(bookId.get()).orElseThrow(
                        () -> {
                            log.error("Get book detail fail");
                            return new Exception("Book is not found!");
                        }
                );
                return ResponseEntity.ok(new BookResponse(HttpStatus.OK.value(), "Successfully!", book));
            } else {
                log.info("Missing book id");
                return ResponseEntity.ok(new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Book is not found"));
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> insertBook(InsertBookRequest insertBookRequest) {
        log.info("Post insert book with :{}", insertBookRequest);
        try {
            Book book = new Book();
            book.setName(insertBookRequest.getBookName());
            book.setImage(insertBookRequest.getImage());
            book.setAuthor(insertBookRequest.getAuthor());
            book.setDescription(insertBookRequest.getDescription());
            Date publishedDate = new SimpleDateFormat(("ddMMyyyy"))
                    .parse(insertBookRequest.getPublishedDate()
                         .replaceAll("/","")
                            .replaceAll("-",""));
            book.setPublishedDate(publishedDate);
            book.setPrice(insertBookRequest.getPrice());
            book.setQuantity(insertBookRequest.getQuantity());
            book.setSaleOff(insertBookRequest.getSaleOff());
            Set<BookGroup> bookGroups = new HashSet<>();
            if(insertBookRequest.getBookGroup().size()>0){
                for(Integer i: insertBookRequest.getBookGroup()){
                    BookGroup bookGroup = groupBookRepository.findById(i).orElseThrow(
                            () -> new Exception("Book group is not found!")
                    );
                    bookGroups.add(bookGroup);
                }
                book.setBookGroups(bookGroups);
            }
            else{
                book.setBookGroups(null);
            }
            log.info("book :{}",book);
            bookRepository.save(book);
            log.info("successfully!");
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Successfully!"));
        }
        catch (Exception e){
            log.error("insert fail!");
            return ResponseEntity.ok(new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Fail!"));
        }
    }

    @Override
    public ResponseEntity<?> updateBook(UpdateBookRequest updateBookRequest) {
        log.info("Post update book with :{}", updateBookRequest);
        try{
            Book book = bookRepository.findById(updateBookRequest.getBookId()).orElseThrow(
                    () -> new Exception("Book is not found!")
            );
            book.setQuantity(updateBookRequest.getQuantity());
            book.setImage(updateBookRequest.getImage());
            book.setSaleOff(updateBookRequest.getSaleOff());
            book.setDescription(updateBookRequest.getDescription());
            log.info("book :{}",book);
            bookRepository.save(book);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Successfully!"));
        }
        catch(Exception e){
            log.error("Update fail!"+e);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> deleteBook(Optional<Long> bookId) {
        log.info("Delete book with book id:{}", bookId);
        try{
            Book book = bookRepository.findById(bookId.get()).orElseThrow(
                    () -> new RuntimeException("Book not found!")
            );
            bookRepository.delete(book);
            log.info("Successfully!");
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),"Successfully!"));
        }
        catch (Exception e){
            log.error("delete fail!");
            return ResponseEntity.ok(new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Fail!"));
        }
    }
}
