package org.example.controllers;

import org.example.models.Book;
import org.example.services.BookService;

import java.sql.SQLException;
import java.util.List;

public class BookController {
    private final BookService bookService = new BookService();

    public void createBook(Book book) throws SQLException {
        bookService.createBook(book);
    }

    public Book findBookById(Long id) throws SQLException {
        return bookService.findBookById(id);
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookService.getAllBooks();
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        return bookService.searchBooks(keyword);
    }

    public void updateBook(Book book) throws SQLException {
        bookService.updateBook(book);
    }

    public void deleteBook(Long id) throws SQLException {
        bookService.deleteBook(id);
    }
}
