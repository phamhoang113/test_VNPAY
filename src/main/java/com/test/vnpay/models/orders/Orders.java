package com.test.vnpay.models.orders;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


}
