package com.vnpay.test.book.service.bookservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertBookRequest {
    @JsonProperty(value = "book_name",required = true)
    private String bookName;
    private String image;
    private String description;
    private String author;
    @JsonProperty(value = "published_date",required = true)
    private String publishedDate;
    @JsonProperty(value = "price",required = true)
    private double price;
    @JsonProperty(value = "quantity", required = true)
    private int quantity;
    @JsonProperty(value = "sale_off")
    private Double saleOff;
    @JsonProperty(value = "book_group")
    private Set<Integer> bookGroup;
}
