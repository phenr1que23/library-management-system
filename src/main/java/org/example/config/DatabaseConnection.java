package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    Dotenv dotenv = Dotenv.load();
    private final String url = dotenv.get("DB_URL");
    private final String userName = dotenv.get("DB_USER");
    private final String password = dotenv.get("DB_PASSWORD");

    public Connection getConnection() throws SQLException {
        assert url != null;
        Connection connection = DriverManager.getConnection(url, userName, password);
        System.out.println("Connected to database");
        return connection;
    }
}
