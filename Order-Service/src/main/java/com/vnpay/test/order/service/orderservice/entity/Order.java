package com.vnpay.test.order.service.orderservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order")
@Setter
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private StatusOrderEnum status;

    @NotNull
    @Column
    private long userId;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_books",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_book_id"))
    private Set<OrderBook> books = new HashSet<>();
}
