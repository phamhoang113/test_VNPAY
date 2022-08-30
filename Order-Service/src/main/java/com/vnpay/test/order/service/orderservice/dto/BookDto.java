package com.vnpay.test.order.service.orderservice.dto;

import lombok.*;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
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
}

@Setter
@Getter
class BookGroup{
    private int id;
    private String name;
}