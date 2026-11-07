
package org.example;

import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.Reader;
import org.example.repositories.BookRepository;
import org.example.repositories.LoanRepository;
import org.example.repositories.ReaderRepository;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws SQLException {
    BookRepository bookRepository = new BookRepository();
        System.out.println(bookRepository.searchBooks("hzauhzauhza"));
    }
}
