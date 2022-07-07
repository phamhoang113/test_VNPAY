package com.test.vnpay.models.orders;

import com.test.vnpay.models.book.Book;
import com.test.vnpay.models.orders.Orders;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "ordersbooks")
public class OrdersBooks {
    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @NotEmpty
    @Column(columnDefinition = "integer default 0")
    private int quantity;

    public OrdersBooks(){}

    public OrdersBooks(Orders orders, Book book, int quantity) {
        this.orders = orders;
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
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
