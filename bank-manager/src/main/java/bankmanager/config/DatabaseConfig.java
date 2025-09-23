package main.java.bankmanager.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
