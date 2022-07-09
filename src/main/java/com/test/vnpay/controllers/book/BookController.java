package com.test.vnpay.controllers.book;

import com.test.vnpay.models.book.Book;
import com.test.vnpay.repository.BookRepository;
import com.test.vnpay.security.exception.InsertErrorException;
import com.test.vnpay.security.exception.ParameterException;
import com.test.vnpay.security.exception.UpdateException;
import com.test.vnpay.security.jwt.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;


    @GetMapping(value = {"/list_book", "/list_books"})
    public List<Book> getListBook(@RequestParam(required = false) String id,
                           @RequestParam(required = false) String name) {
            if (id != null && !id.isEmpty()) {
                try {
                    Long idBook = Long.parseLong(id);
                    List<Book> resultList = new ArrayList<>();
                    Book book = bookRepository.findById(idBook).get();
                    resultList.add(book);
                    return resultList;
                }
                catch (NumberFormatException numberFormatException){
                    throw new ParameterException("id","integer");
                }
                catch (NoSuchElementException noSuchElementException){
                    return new ArrayList<>();
                }
            }
            if (name != null && !name.isEmpty()) {
                try {
                    List<Book> resultList = new ArrayList<>();
                    Book book = bookRepository.findByName(name).get();
                    resultList.add(book);
                    return resultList;
                }
                catch (NoSuchElementException e){
                    return new ArrayList<>();
                }
            }
            return bookRepository.findAll();
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/add_book"})
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book){
        try {
            Book book1 = new Book(book.getName(), book.getImage(), book.getDescription(), book.getAuthor(), book.getPublishedDate()
            , book.getPrice(), book.getQuantity(), book.getBookGroups());
            bookRepository.save(book1);
            return ResponseEntity.ok(new MessageResponse("Insert Successfully!"));
        }
        catch (Exception e){
            throw new InsertErrorException();
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/modify_book"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody Book book){
        try {
            bookRepository. save(book);
            return ResponseEntity.ok(new MessageResponse("Update successfully!"));
        }
        catch (Exception e){
            throw new UpdateException();
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/delete_book"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody String id){
        try {
            bookRepository.deleteById(Long.parseLong(id));
            return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
        }
        catch (NumberFormatException nbEx){
            throw new ParameterException("id", "integer");
        }
        catch (Exception e){
            throw new UpdateException();
        }
    }

}
