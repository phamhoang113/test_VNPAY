package com.vnpay.test.book.service.bookservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vnpay.test.book.service.bookservice.entity.Book;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class ListBookResponse extends BaseResponse{
    @JsonProperty("books")
    private List<Book> books;

    @JsonProperty("quantity")
    private long quantity;
    public ListBookResponse(int code, String desc, long quantity, Page<Book> books){
        super(code, desc);
        this.quantity = quantity;
        this.books = books.toList();
    }
}
