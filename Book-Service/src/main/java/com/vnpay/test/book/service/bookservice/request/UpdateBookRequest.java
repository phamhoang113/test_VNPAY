package com.vnpay.test.book.service.bookservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {
    @JsonProperty(value = "book_id", required = true)
    private long bookId;
    private String image;
    private int quantity;
    private String description;
    @JsonProperty(value = "sale_off")
    private double saleOff;
}
