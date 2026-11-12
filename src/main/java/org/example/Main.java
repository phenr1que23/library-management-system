
package org.example;

import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.Reader;
import org.example.repositories.BookRepository;
import org.example.repositories.LoanRepository;
import org.example.repositories.ReaderRepository;
import org.example.services.BookService;
import org.example.services.LoanService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        BookService bookService = new BookService();
        LoanService loanService = new LoanService();
        Book book = bookService.findBookById(13L);

        bookService.deleteBook(3L);

    }
}
