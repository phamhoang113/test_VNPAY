package com.vnpay.test.order.service.orderservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "order_book")
@Setter
@Getter
@NoArgsConstructor
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int bookId;

    @NotEmpty
    @Column(columnDefinition = "number(12,0) default 0")
    private int quantity;
}

