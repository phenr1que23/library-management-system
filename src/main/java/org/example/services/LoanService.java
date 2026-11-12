package org.example.services;

import org.example.config.DatabaseConnection;
import org.example.models.Book;
import org.example.models.Loan;
import org.example.models.Reader;
import org.example.repositories.BookRepository;
import org.example.repositories.LoanRepository;
import org.example.repositories.ReaderRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final LoanRepository loanRepository = new LoanRepository();
    private final BookRepository bookRepository = new BookRepository();
    private final ReaderRepository readerRepository = new ReaderRepository();

    public void createLoan(Loan loan) throws SQLException{
        validateLoan(loan);
        Book book = this.bookRepository.getBookById(loan.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found."));
        Reader reader = this.readerRepository.getReaderById(loan.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found."));

        if (!reader.isActive()) {
            throw new RuntimeException("Reader is inactive.");
        }
        if(book.getAvailableQuantity() <= 0){
            throw new RuntimeException("Without sufficient book.");
        }

        LocalDate loanDate = loan.getLoanDate();
        if (loanDate == null) {
            loanDate = LocalDate.now();
        }
        loan.setLoanDate(loanDate);
        LocalDate expectedReturnDate = loanDate.plusDays(14);
        loan.setExpectedReturnDate(expectedReturnDate);
        loan.setEffectiveReturnDate(null);
        loan.setActive(true);

        try(Connection connection = new DatabaseConnection().getConnection()){
            connection.setAutoCommit(false);
            try{
                loanRepository.createLoan(connection, loan);
                bookRepository.decreaseAvailableQuantity(connection, loan.getBookId());
                connection.commit();
            }catch (SQLException | RuntimeException e){
                try {
                    connection.rollback();
                } catch (SQLException rollbackException){
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        }
    }

    public Loan findLoanById(Long id) throws SQLException{
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid loan ID.");
        }
        return this.loanRepository.getLoanById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found."));
    }

    public List<Loan> getAllLoans() throws SQLException{
        return this.loanRepository.getAllLoans();
    }

    public List<Loan> getActiveLoans() throws  SQLException{
        return this.loanRepository.getActiveLoans();
    }

    public List<Loan> findLoansByReader(Long readerId) throws SQLException{
        if(readerId == null || readerId <= 0){
            throw new IllegalArgumentException("Invalid reader ID.");
        }
        return this.loanRepository.getLoansByReader(readerId);
    }

    public void returnLoan (Long id, LocalDate effectiveReturnDate) throws SQLException {
        if(effectiveReturnDate == null){
            throw new IllegalArgumentException("Effective return date is required.");
        }
        if (effectiveReturnDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Return date cannot be in the future."
            );
        }

        Loan loan = findLoanById(id);
        if (effectiveReturnDate.isBefore(loan.getLoanDate())) {
            throw new IllegalArgumentException(
                    "Return date cannot be before loan date."
            );
        }
        if (!loan.getActive()) {
            throw new RuntimeException("Loan inactive.");
        }
        try (Connection connection = new DatabaseConnection().getConnection()) {
            connection.setAutoCommit(false);
            try {
                loanRepository.registerEffectiveReturn(connection, id, effectiveReturnDate);
                bookRepository.increaseAvailableQuantity(connection, loan.getBookId());
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        }
    }

    public void validateLoan(Loan loan){
        if(loan == null){
            throw new IllegalArgumentException("Loan cannot be null.");
        }
        if(loan.getBookId() == null || loan.getBookId() <= 0){
            throw new IllegalArgumentException("Book ID is required.");
        }
        if (loan.getReaderId() == null || loan.getReaderId() <= 0) {
            throw new IllegalArgumentException("Reader ID is required.");
        }
    }
}
