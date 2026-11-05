package org.example.repositories;

import org.example.config.DatabaseConnection;
import org.example.models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public void createBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, " +
                "publication_date, total_copies, available_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                if (book.getPublicationDate() != null) {
                    pstmt.setDate(4, java.sql.Date.valueOf(book.getPublicationDate()));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setInt(5, book.getTotalOfCopies());
                pstmt.setInt(6, book.getAvailableQuantity());
        pstmt.executeUpdate();
        }
    }
    public List<Book> getAllBooks() throws SQLException{
        String sql = "SELECT id, title, author, isbn, publication_date, " +
                "total_copies, available_quantity FROM books";
        List<Book> books = new ArrayList<>();
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while(rs.next()){
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                java.sql.Date sqlDate = rs.getDate("publication_date");
                LocalDate publicationDate =
                        sqlDate != null ? sqlDate.toLocalDate() : null;
                Integer totalOfCopies= rs.getInt("total_copies");
                Integer availableQuantity = rs.getInt("available_quantity");
                books.add(new Book(id, title, author, isbn, publicationDate, totalOfCopies, availableQuantity));
            }
            return books;
        }
    }
    public Book getBookById(Long id) throws SQLException {
        String sql = "SELECT id, title, author, isbn, publication_date, " +
                "total_copies, available_quantity FROM books WHERE id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Long bookId = rs.getLong("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    java.sql.Date sqlDate = rs.getDate("publication_date");
                    LocalDate publicationDate =
                            sqlDate != null ? sqlDate.toLocalDate() : null;
                    Integer totalOfCopies = rs.getInt("total_copies");
                    Integer availableQuantity = rs.getInt("available_quantity");
                    return new Book(bookId, title, author, isbn,
                            publicationDate, totalOfCopies, availableQuantity);
                }
                return null;
            }
        }
    }

    public void updateBook(Book book) throws SQLException{
        String sql = "UPDATE books SET title=?, author=?, isbn=?, publication_date=?, " +
                "total_copies=?, available_quantity=? WHERE id=?";
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                if (book.getPublicationDate() != null) {
                    pstmt.setDate(4, java.sql.Date.valueOf(book.getPublicationDate()));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setInt(5, book.getTotalOfCopies());
                pstmt.setInt(6, book.getAvailableQuantity());
                pstmt.setLong(7, book.getId());
            int rowsAffected = pstmt.executeUpdate();
        }
    }

    public void deleteBook(Long id) throws SQLException{
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, id);
                int rowsAffected = pstmt.executeUpdate();
        }
    }
}
