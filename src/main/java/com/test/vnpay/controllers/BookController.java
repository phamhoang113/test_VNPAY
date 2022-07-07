package com.test.vnpay.controllers;

import com.test.vnpay.models.book.Book;
import com.test.vnpay.models.user.Role;
import com.test.vnpay.models.user.RoleEnum;
import com.test.vnpay.repository.BookrRpository;
import com.test.vnpay.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    BookrRpository bookrRpository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping(value = {"/list_book","/list_books"})
    List<Book> getListBook(@RequestParam(required = false) String id){;
        bookrRpository.save(new Book("test 1","http://300b5338.vws.vegacdn.vn/image/img.news/0/0/0/275.jpg?v=1&w=628&h=365&nocache=1","test book 1","PPH",new Date(),0, 10000, new HashSet<>()));
        return bookrRpository.findAll();
    }



}
