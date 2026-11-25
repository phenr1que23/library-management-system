
package org.example.controllers;

import org.example.models.Loan;
import org.example.services.LoanService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanController {

    private final LoanService loanService = new LoanService();

    public void createLoan(Loan loan) throws SQLException {
        loanService.createLoan(loan);
    }

    public Loan findLoanById(Long id) throws SQLException {
        return loanService.findLoanById(id);
    }

    public List<Loan> getAllLoans() throws SQLException {
        return loanService.getAllLoans();
    }

    public List<Loan> getActiveLoans() throws SQLException {
        return loanService.getActiveLoans();
    }

    public List<Loan> findLoansByReader(Long readerId) throws SQLException {
        return loanService.findLoansByReader(readerId);
    }

    public void returnLoan(Long id, LocalDate effectiveReturnDate) throws SQLException {
        loanService.returnLoan(id, effectiveReturnDate);
    }
}
