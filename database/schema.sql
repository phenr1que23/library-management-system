CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

CREATE TABLE books
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(255)       NOT NULL,
    author             VARCHAR(255)       NOT NULL,
    isbn               VARCHAR(20) UNIQUE NOT NULL,
    publication_date   DATE,
    total_copies       INT                NOT NULL CHECK (total_copies >= 0),
    available_quantity INT                NOT NULL CHECK (available_quantity >= 0),
    CHECK (available_quantity <= total_copies)
);

CREATE TABLE readers
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name         VARCHAR(255)        NOT NULL,
    email             VARCHAR(255) UNIQUE NOT NULL,
    phone             VARCHAR(15)           NOT NULL,
    registration_date DATE                NOT NULL,
    is_active         BOOLEAN             NOT NULL DEFAULT TRUE
);

CREATE TABLE loans
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    reader_id             BIGINT NOT NULL,
    loan_date             DATE   NOT NULL,
    expected_return_date  DATE   NOT NULL,
    effective_return_date DATE   NULL,
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_loan_reader  FOREIGN KEY (reader_id) REFERENCES readers(id)
);
/*
*/