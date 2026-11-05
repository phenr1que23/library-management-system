package org.example;

import org.example.config.DatabaseConnection;

import java.sql.SQLException;

public class Main {
    static void main() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.getConnection();
    }
}
