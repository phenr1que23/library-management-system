    package org.example.repositories;

    import org.example.config.DatabaseConnection;
    import org.example.models.Loan;
    import org.example.models.Reader;

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
                    "expected_return_date, effective_return_date, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
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
                    pstmt.setBoolean(6, true);
                pstmt.executeUpdate();
            }
        }
        public List<Loan> getAllLoans() throws SQLException{
            String sql = "SELECT id, book_id, reader_id, loan_date, " +
                    "expected_return_date, effective_return_date, is_active " +
                    "FROM loans";
            List<Loan> loans = new ArrayList<>();
            try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    loans.add(resultSetToLoan(rs));
                }
            return loans;
            }
        }
        public void registerLoanReturn(Long id, LocalDate effectiveReturnDate) throws SQLException{
            String sql = "UPDATE loans SET effective_return_date=?, is_active=false WHERE id=? AND effective_return_date IS NULL";
            try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setDate(1, java.sql.Date.valueOf(effectiveReturnDate));
                pstmt.setLong(2, id);
                int rowsAffected = pstmt.executeUpdate();
            }
        }

        public List<Loan> getActiveLoans() throws SQLException{
            String sql = "SELECT id, book_id, reader_id, loan_date, " +
                    "expected_return_date, effective_return_date, is_active FROM loans " +
                    "WHERE is_active = true ORDER BY id";
            List<Loan> activeLoans = new ArrayList<>();
            try(Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    activeLoans.add(resultSetToLoan(rs));
                }
                return activeLoans;
            }
        }
        public Loan getLoanById(Long id) throws SQLException {
            String sql = "SELECT id, book_id, reader_id, loan_date, " +
                    "expected_return_date, effective_return_date, is_active FROM loans " +
                    "WHERE id = ? ORDER BY id";
            try (Connection connection = new DatabaseConnection().getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if(rs.next()) {
                        return resultSetToLoan(rs);
                    }else{
                        return null;
                    }
                }
            }
        }

        public List<Loan> getLoansByReader(Reader reader) throws SQLException{
            String sql = "SELECT id, book_id, reader_id, loan_date," +
                    "expected_return_date, effective_return_date, is_active FROM loans " +
                    "WHERE reader_id=? ORDER BY id";
            List<Loan> loans = new ArrayList<>();
            try(Connection connection = new DatabaseConnection().getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, reader.getId());
                try (ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        loans.add(resultSetToLoan(rs));
                    }
                    return loans;
                }
            }
        }

        public boolean hasActiveLoanByBookId(Long bookId) throws SQLException{
            String sql = "SELECT id, book_id, reader_id FROM loans " +
                    "WHERE book_id = ? AND is_active = TRUE";
            try (Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, bookId);
                try(ResultSet rs = pstmt.executeQuery()){
                   return rs.next();
                }
            }
        }

        private Loan resultSetToLoan(ResultSet rs) throws SQLException{
            Long id = rs.getLong("id");
            Long bookId = rs.getLong("book_id");
            Long readerId = rs.getLong("reader_id");
            LocalDate loanDate = rs.getDate("loan_date").toLocalDate();
            LocalDate expectedReturnDate = rs.getDate("expected_return_date").toLocalDate();
            java.sql.Date sqlDate = rs.getDate("effective_return_date");
            LocalDate effectiveReturnDate =
                    sqlDate != null ? sqlDate.toLocalDate() : null;
            Boolean isActive = rs.getBoolean("is_active");
            return new Loan(id, bookId, readerId, loanDate, expectedReturnDate, effectiveReturnDate, isActive);
        }
    }
