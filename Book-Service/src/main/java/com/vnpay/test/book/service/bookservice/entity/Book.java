package com.vnpay.test.book.service.bookservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
@Setter
@Getter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String image;

    @Size(max = 200)
    private String description;

    @NotBlank
    @Size(max = 200)
    private String author;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date publishedDate;

    @Column(columnDefinition = "number(19,2) default 0")
    private double price;

    @NotNull
    @Column(columnDefinition = "number(19,0) default 0")
    private int quantity;

    @Column(columnDefinition = "number(19,2) default 0")
    private double saleOff;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_types",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "bookgroup_id"))
    private Set<BookGroup> bookGroups = new HashSet<>();
}