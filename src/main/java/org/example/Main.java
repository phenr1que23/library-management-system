
package org.example;

import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.Reader;
import org.example.repositories.BookRepository;
import org.example.repositories.LoanRepository;
import org.example.repositories.ReaderRepository;
import org.example.services.BookService;
import org.example.services.LoanService;
import org.example.services.ReaderService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        ReaderService readerService = new ReaderService();
        Reader reader = new Reader();

        reader.setId(999L);
        reader.setFullName("Leitor Inexistente");
        reader.setEmail("inexistente@gmail.com");
        reader.setPhone("19977777777");


        readerService.updateReader(reader);
    }
}
