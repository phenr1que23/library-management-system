
package org.example.controllers;

import org.example.models.Reader;
import org.example.services.ReaderService;

import java.sql.SQLException;
import java.util.List;

public class ReaderController {

    private final ReaderService readerService = new ReaderService();

    public void createReader(Reader reader) throws SQLException {
        readerService.createReader(reader);
    }

    public Reader findReaderById(Long id) throws SQLException {
        return readerService.findReaderById(id);
    }

    public List<Reader> getAllReaders() throws SQLException {
        return readerService.getAllReaders();
    }

    public List<Reader> searchReaders(String keyword) throws SQLException {
        return readerService.searchReaders(keyword);
    }

    public void updateReader(Reader reader) throws SQLException {
        readerService.updateReader(reader);
    }

    public void changeActiveStatus(Long id, boolean isActive) throws SQLException {
        readerService.changeActiveStatus(id, isActive);
    }

    public void deleteReader(Long id) throws SQLException {
        readerService.deleteReader(id);
    }
}
