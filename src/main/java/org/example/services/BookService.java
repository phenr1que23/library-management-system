package org.example.services;

import org.example.config.DatabaseConnection;
import org.example.models.Book;
import org.example.repositories.BookRepository;
import org.example.repositories.LoanRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    final private BookRepository bookRepository = new BookRepository();
    final private LoanRepository loanRepository = new LoanRepository();

    public void createBook(Book book) throws SQLException {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        book.setAvailableQuantity(book.getTotalOfCopies());
        validateBook(book);
        this.bookRepository.createBook(book);
    }

    public Book findBookById(Long id) throws  SQLException{
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }
        return this.bookRepository.getBookById(id).orElseThrow(() ->
                new RuntimeException("Book not found."));
    }

    public List<Book> getAllBooks() throws SQLException{
        return this.bookRepository.getAllBooks();
    }

    public List<Book> searchBooks(String keyword) throws SQLException{
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }
        String preparedKeyword = keyword.trim().toLowerCase();
        return this.bookRepository.searchBooks(preparedKeyword);
    }

    public void updateBook(Book book) throws SQLException{
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (book.getId() == null || book.getId() <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }
        if (book.getTotalOfCopies() == null || book.getTotalOfCopies() < 0) {
            throw new IllegalArgumentException("Invalid total copies.");
        }

        Book existingBook = this.bookRepository.getBookById(book.getId())
                .orElseThrow(() -> new RuntimeException("Book not found."));

        int borrowedCopies = existingBook.getTotalOfCopies()
                - existingBook.getAvailableQuantity();

        book.setAvailableQuantity(book.getTotalOfCopies() - borrowedCopies);

        if (book.getTotalOfCopies() < borrowedCopies) {
            throw new IllegalArgumentException(
                    "Total copies cannot be less than borrowed copies."
            );
        }
        try (Connection connection = new DatabaseConnection().getConnection()) {
            this.bookRepository.updateBook(connection, book);
        }
    }

    public void deleteBook(Long id) throws SQLException{
        findBookById(id);
        if (loanRepository.hasActiveLoanByBookId(id)) {
            throw new IllegalArgumentException(
                    "Cannot delete a book with active loans."
            );
        }
        bookRepository.deleteBook(id);
    }

    public void validateBook(Book book){
        if(book == null){
            throw new IllegalArgumentException("Book cannot be null.");
        }

        if (book.getTotalOfCopies() == null || book.getTotalOfCopies() < 0) {
            throw new IllegalArgumentException("Invalid total copies.");
        }

        if (book.getAvailableQuantity() == null || book.getAvailableQuantity() < 0) {
            throw new IllegalArgumentException("Invalid available quantity.");
        }

        if(book.getAvailableQuantity() > book.getTotalOfCopies()){
            throw new IllegalArgumentException("Available quantity cannot be greater than total copies.");
        }
    }
}
