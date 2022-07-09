package com.test.vnpay.models.orders;

import com.test.vnpay.models.user.Role;
import com.test.vnpay.models.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private Date date;

    @NotNull
    private StatusOrderEnum status;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "order_books",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_book_id"))
    private Set<OrdersBooks> books = new HashSet<>();

    public Orders(){}

    public Orders(Date date, StatusOrderEnum status, User user) {
        this.date = date;
        this.status = status;
        this.user = user;
    }

    public Orders(Date date, StatusOrderEnum status, User user, Set<OrdersBooks> books) {
        this.date = date;
        this.status = status;
        this.user = user;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StatusOrderEnum getStatus() {
        return status;
    }

    public void setStatus(StatusOrderEnum status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<OrdersBooks> getBooks() {
        return books;
    }

    public void setBooks(Set<OrdersBooks> books) {
        this.books = books;
    }
}
