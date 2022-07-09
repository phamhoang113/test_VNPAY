package com.test.vnpay.models.orders;

import com.test.vnpay.models.book.Book;
import com.test.vnpay.models.orders.Orders;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "ordersbooks")
public class OrdersBooks {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @NotEmpty
    @Column(columnDefinition = "integer default 0")
    private int quantity;

    public OrdersBooks(){}

    public OrdersBooks(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
