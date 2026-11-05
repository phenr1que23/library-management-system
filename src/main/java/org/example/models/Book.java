package org.example.models;

import java.time.LocalDate;
import java.util.Date;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private Integer totalOfCopies;
    private Integer availableQuantity;

    public Book(Long id, String title, String author, String isbn, LocalDate publicationDate, Integer totalOfCopies, Integer availableQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.totalOfCopies = totalOfCopies;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getTotalOfCopies() {
        return totalOfCopies;
    }

    public void setTotalOfCopies(Integer totalOfCopies) {
        this.totalOfCopies = totalOfCopies;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public String toString() {
        return "Id: " + id.toString() + " Title: " + title;
    }
}
