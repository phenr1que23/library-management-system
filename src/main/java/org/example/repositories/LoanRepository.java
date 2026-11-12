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
    import java.util.Optional;

    public class LoanRepository {
        public void createLoan(Connection connection, Loan loan) throws SQLException {
            String sql = "INSERT INTO loans (book_id, reader_id, loan_date, " +
                    "expected_return_date, effective_return_date, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, loan.getBookId());
                pstmt.setLong(2, loan.getReaderId());
                pstmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
                pstmt.setDate(4, java.sql.Date.valueOf(loan.getExpectedReturnDate()));
                if(loan.getEffectiveReturnDate() != null){
                    pstmt.setDate(5, java.sql.Date.valueOf(loan.getEffectiveReturnDate()));
                }else{
                    pstmt.setNull(5, java.sql.Types.DATE);
                }
                pstmt.setBoolean(6, loan.getActive());
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
        public void registerEffectiveReturn(Connection connection, Long id, LocalDate effectiveReturnDate) throws SQLException{
            String sql = "UPDATE loans SET effective_return_date=?, is_active=FALSE WHERE id=? AND effective_return_date IS NULL AND is_active = TRUE";
            try(PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setDate(1, java.sql.Date.valueOf(effectiveReturnDate));
                pstmt.setLong(2, id);
                int rowsAffected = pstmt.executeUpdate();
                if(rowsAffected == 0){
                    throw new SQLException("Unable to register loan return.");
                }
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
        public Optional<Loan> getLoanById(Long id) throws SQLException {
            String sql = "SELECT id, book_id, reader_id, loan_date, " +
                    "expected_return_date, effective_return_date, is_active FROM loans " +
                    "WHERE id = ?";
            try (Connection connection = new DatabaseConnection().getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if(rs.next()) {
                        return Optional.of(resultSetToLoan(rs));
                    }else{
                        return Optional.empty();
                    }
                }
            }
        }

        public List<Loan> getLoansByReader(Long readerId) throws SQLException{
            String sql = "SELECT id, book_id, reader_id, loan_date," +
                    "expected_return_date, effective_return_date, is_active FROM loans " +
                    "WHERE reader_id=? ORDER BY id";
            List<Loan> loans = new ArrayList<>();
            try(Connection connection = new DatabaseConnection().getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)){
                pstmt.setLong(1, readerId);
                try (ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        loans.add(resultSetToLoan(rs));
                    }
                    return loans;
                }
            }
        }

        public boolean hasActiveLoanByBookId(Long bookId) throws SQLException{
            String sql = "SELECT 1 FROM loans " +
                    "WHERE book_id = ? AND is_active = TRUE LIMIT 1";
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
