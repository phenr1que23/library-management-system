package org.example.models;

import java.time.LocalDate;

public class Loan {
    private Long id;
    private Long bookId;
    private Long readerId;
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;
    private LocalDate effectiveReturnDate;

    public Loan(Long id, Long bookId, Long readerId, LocalDate loanDate, LocalDate expectedReturnDate, LocalDate effectiveReturnDate) {
        this.id = id;
        this.bookId = bookId;
        this.readerId = readerId;
        this.loanDate = loanDate;
        this.expectedReturnDate = expectedReturnDate;
        this.effectiveReturnDate = effectiveReturnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getEffectiveReturnDate() {
        return effectiveReturnDate;
    }

    public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
        this.effectiveReturnDate = effectiveReturnDate;
    }
}
