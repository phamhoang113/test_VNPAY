package com.test.vnpay.models.book;

import com.test.vnpay.models.user.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Date publishedDate;

    @Column(columnDefinition = "double default 0")
    private double price;

    @NotNull
    @Column(columnDefinition = "integer default 0")
    private int quantity;

    private double saleOff;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "book_types",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "bookgroup_id"))
    private Set<BookGroup> bookGroups = new HashSet<>();

    public Book(){}

    public Book(String name,String image, String description, String author, Date publishedDate, double price, int quantity, Set<BookGroup> bookGroups) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.image = image;
        this.publishedDate = publishedDate;
        this.price = price;
        this.quantity = quantity;
        this.bookGroups = bookGroups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<BookGroup> getBookGroups() {
        return bookGroups;
    }

    public void setBookGroups(Set<BookGroup> bookGroups) {
        this.bookGroups = bookGroups;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(double saleOff) {
        this.saleOff = saleOff;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
