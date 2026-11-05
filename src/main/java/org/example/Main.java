
package org.example;

import org.example.models.Book;
import org.example.repositories.BookRepository;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws SQLException {

        BookRepository repository = new BookRepository();

        Book[] books = {
                new Book(null, "Clean Code", "Robert C. Martin",
                        "9780132350884", LocalDate.of(2008, 8, 1), 5, 5),

                new Book(null, "Effective Java", "Joshua Bloch",
                        "9780134685991", LocalDate.of(2017, 12, 27), 4, 4),

                new Book(null, "The Pragmatic Programmer", "David Thomas",
                        "9780135957059", LocalDate.of(2019, 9, 13), 3, 3),

                new Book(null, "Design Patterns", "Erich Gamma",
                        "9780201633610", LocalDate.of(1994, 10, 31), 2, 2),

                new Book(null, "Refactoring", "Martin Fowler",
                        "9780134757599", LocalDate.of(2018, 11, 19), 3, 3),

                new Book(null, "Head First Java", "Kathy Sierra",
                        "9780596009205", LocalDate.of(2005, 2, 9), 6, 6),

                new Book(null, "Java: The Complete Reference", "Herbert Schildt",
                        "9781260440232", LocalDate.of(2018, 12, 6), 4, 4),

                new Book(null, "Introduction to Algorithms", "Thomas H. Cormen",
                        "9780262046305", LocalDate.of(2022, 4, 5), 2, 2),

                new Book(null, "Domain-Driven Design", "Eric Evans",
                        "9780321125217", LocalDate.of(2003, 8, 30), 3, 3),

                new Book(null, "Spring in Action", "Craig Walls",
                        "9781617297571", LocalDate.of(2022, 6, 19), 4, 4),

                new Book(null, "Database System Concepts", "Abraham Silberschatz",
                        "9780078022159", LocalDate.of(2019, 7, 5), 5, 5),

                new Book(null, "Computer Networking", "Andrew S. Tanenbaum",
                        "9780132126953", LocalDate.of(2010, 10, 7), 3, 3)
        };

        //for (Book book : books) {
        //   repository.createBook(book);
        //}

        //System.out.println("Books successfully registered!");
        //System.out.println(repository.getAllBooks());
        System.out.println(repository.getBookById(4L).toString());
    }
}
