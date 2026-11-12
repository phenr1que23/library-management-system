package org.example.repositories;

import org.example.config.DatabaseConnection;
import org.example.models.Book;
import org.example.models.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderRepository {
    public void createReader(Reader reader) throws SQLException {
        String sql = "INSERT INTO readers (full_name, email, phone, is_active) " +
                "VALUES (?, ?, ?, ?)";
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setString(1, reader.getFullName());
                pstmt.setString(2, reader.getEmail());
                pstmt.setString(3, reader.getPhone());
                pstmt.setBoolean(4, reader.isActive());
            pstmt.executeUpdate();
        }
    }

    public List<Reader> getAllReaders() throws SQLException{
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT id, full_name, email, phone, registration_date, is_active FROM readers ORDER BY id";
        try(Connection connection = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                readers.add(resultSetToReader(rs));
            }
        return readers;
        }
    }

    public Optional<Reader> getReaderById(Long id) throws SQLException{
        String sql = "SELECT id, full_name, email, phone, registration_date, is_active FROM readers " +
                "WHERE id = ?";
        try(Connection connection = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){
        pstmt.setLong(1, id);
        try (ResultSet rs = pstmt.executeQuery()){
            if(rs.next()){
                return Optional.of(resultSetToReader(rs));
            }
            return Optional.empty();
            }
        }
    }
    public void updateReader(Reader reader) throws SQLException{
        String sql = "UPDATE readers SET full_name=?, email=?, phone=?, " +
                "is_active=? WHERE id=?";
        try(Connection connection = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, reader.getFullName());
            pstmt.setString(2, reader.getEmail());
            pstmt.setString(3, reader.getPhone());
            pstmt.setBoolean(4, reader.isActive());
            pstmt.setLong(5, reader.getId());
        int rowsAffected = pstmt.executeUpdate();
        }
    }

    public void changeActiveStatus(Long id, boolean isActive) throws SQLException{
        String sql = "UPDATE readers SET is_active=? WHERE id=?";
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setBoolean(1, isActive);
            pstmt.setLong(2, id);
            int rowsAffected = pstmt.executeUpdate();
        }
    }

    public void deleteReader(Long id) throws SQLException{
        String sql = "DELETE FROM readers WHERE id=?";
        try(Connection connection = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setLong(1, id);
        int rowsAffected = pstmt.executeUpdate();
        }
    }

    public List<Reader> searchReaders(String keyword) throws SQLException{
        String sql = "SELECT id, full_name, email, phone, registration_date, " +
                "is_active FROM readers " +
                "WHERE full_name LIKE ? OR email LIKE ? OR phone LIKE ?";
        List<Reader> results = new ArrayList<>();
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    results.add(resultSetToReader(rs));
                }
                return results;
            }
        }
    }

    private Reader resultSetToReader(ResultSet rs) throws SQLException{
        Long readerId = rs.getLong("id");
        String fullName = rs.getString("full_name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        LocalDate registrationDate = rs.getDate("registration_date").toLocalDate();
        Boolean isActive = rs.getBoolean("is_active");
        return new Reader(readerId, fullName, email, phone, registrationDate, isActive);
    }

}
