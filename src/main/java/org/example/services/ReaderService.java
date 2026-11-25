package org.example.services;

import org.apache.commons.validator.routines.EmailValidator;
import org.example.models.Reader;
import org.example.repositories.ReaderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ReaderService {
    private final ReaderRepository readerRepository = new ReaderRepository();
    private final EmailValidator validator = EmailValidator.getInstance();

    public void createReader(Reader reader) throws SQLException {
        validateReader(reader);
        String normalizedEmail = normalizeEmail(reader.getEmail());
        String normalizedPhone = normalizePhone(reader.getPhone());
        validateEmail(normalizedEmail);
        validatePhone(normalizedPhone);
        reader.setFullName(reader.getFullName().trim());
        reader.setEmail(normalizedEmail);
        reader.setPhone(normalizedPhone);
        readerRepository.createReader(reader);
    }

    public void updateReader(Reader reader) throws SQLException{
        validateReader(reader);
        if (reader.getId() == null) {
            throw new IllegalArgumentException("Reader ID cannot be null.");
        }
        readerRepository.getReaderById(reader.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reader not found."));
        String normalizedEmail = normalizeEmail(reader.getEmail());
        String normalizedPhone = normalizePhone(reader.getPhone());
        validateEmailForUpdate(normalizedEmail, reader.getId());
        validatePhone(normalizedPhone);
        reader.setFullName(reader.getFullName().trim());
        reader.setEmail(normalizedEmail);
        reader.setPhone(normalizedPhone);
        readerRepository.updateReader(reader);
    }

    private void validateReader(Reader reader){
        if(reader == null){
            throw new IllegalArgumentException("Reader cannot be null.");
        }
        if (reader.getFullName() == null || reader.getFullName().isBlank()) {
            throw new IllegalArgumentException("Reader name cannot be empty.");
        }
        if (reader.getEmail() == null || reader.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (reader.getPhone() == null || reader.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }
    }
    private void validateEmailFormat(String email) {
        if (!validator.isValid(email)) {
            throw new IllegalArgumentException("Invalid email.");
        }
    }

    private void validateEmail(String email) throws SQLException{
        validateEmailFormat(email);
        if (readerRepository.getReaderByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Someone already has this email address."
            );
        }
    }

    private void validateEmailForUpdate(String email, Long readerId) throws SQLException {
        validateEmailFormat(email);
        if (readerRepository.getReaderByEmail(email)
                .filter(existingReader -> !readerId.equals(existingReader.getId()))
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Someone already has this email address."
            );
        }
    }

    private  void validatePhone(String phone){
        if (!phone.matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Invalid phone number.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePhone(String phone) {
        return phone.replaceAll("\\D", "");
    }

    public Reader findReaderById(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid reader ID.");
        }

        return readerRepository.getReaderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reader not found."));
    }

    public List<Reader> getAllReaders() throws SQLException {
        return readerRepository.getAllReaders();
    }

    public List<Reader> searchReaders(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }

        return readerRepository.searchReaders(keyword.trim());
    }

    public void changeActiveStatus(Long id, boolean isActive) throws SQLException {
        findReaderById(id);
        readerRepository.changeActiveStatus(id, isActive);
    }

    public void deleteReader(Long id) throws SQLException {
        findReaderById(id);
        readerRepository.deleteReader(id);
    }
}
