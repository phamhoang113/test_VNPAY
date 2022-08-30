package com.vnpay.test.order.service.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookOrder{
    @JsonProperty("book_id")
    private String bookId;
    @JsonProperty("quantity")
    private int quantity;
}
