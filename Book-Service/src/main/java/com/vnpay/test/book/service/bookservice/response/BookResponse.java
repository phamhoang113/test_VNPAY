package com.vnpay.test.book.service.bookservice.response;

import com.vnpay.test.book.service.bookservice.entity.Book;
import com.vnpay.test.book.service.bookservice.entity.BookGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class BookResponse extends BaseResponse{
    private Long id;
    private String name;
    private String image;
    private String description;
    private String author;
    private Date publishedDate;
    private double price;
    private int quantity;
    private double saleOff;
    private Set<BookGroup> bookGroups;

    public BookResponse(int code, String desc, Book book) {
        super(code, desc);
        this.id = book.getId();
        this.name = book.getName();
        this.image = book.getImage();
        this.description = book.getDescription();
        this.author = book.getAuthor();
        this.publishedDate = book.getPublishedDate();
        this.price = book.getPrice();
        this.quantity = book.getQuantity();
        this.saleOff = book.getSaleOff();
        this.bookGroups = getBookGroups();
    }
}
