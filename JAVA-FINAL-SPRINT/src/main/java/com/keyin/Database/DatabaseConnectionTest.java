package com.keyin.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            System.out.println("Database connected successfully!");

            // Test query
            conn.createStatement().execute("SELECT 1");
            System.out.println("Query executed successfully!");

            conn.close();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}