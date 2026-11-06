package org.example.repositories;

import org.example.config.DatabaseConnection;
import org.example.models.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {
    public void createLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (book_id, reader_id, loan_date, " +
                "expected_return_date, effective_return_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, loan.getBookId());
                pstmt.setLong(2, loan.getReaderId());
                pstmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
                pstmt.setDate(4, java.sql.Date.valueOf(loan.getExpectedReturnDate()));
                if(loan.getEffectiveReturnDate() != null){
                    pstmt.setDate(5, java.sql.Date.valueOf(loan.getEffectiveReturnDate()));
                }else{
                    pstmt.setNull(5, java.sql.Types.DATE);
                }
            pstmt.executeUpdate();
        }
    }
    public List<Loan> getAllLoans() throws SQLException{
        String sql = "SELECT id, book_id, reader_id, loan_date, " +
                "expected_return_date, effective_return_date " +
                "FROM loans";
        List<Loan> loans = new ArrayList<>();
        try(Connection connection = new DatabaseConnection().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
            while(rs.next()){
                Long id = rs.getLong("id");
                Long bookId = rs.getLong("book_id");
                Long readerId = rs.getLong("reader_id");
                LocalDate loanDate = rs.getDate("loan_date").toLocalDate();
                LocalDate expectedReturnDate = rs.getDate("expected_return_date").toLocalDate();
                java.sql.Date sqlDate = rs.getDate("effective_return_date");
                LocalDate effectiveReturnDate =
                        sqlDate != null ? sqlDate.toLocalDate() : null;
                loans.add(new Loan(id, bookId, readerId, loanDate, expectedReturnDate, effectiveReturnDate));
            }
        return loans;
        }
    }
}
