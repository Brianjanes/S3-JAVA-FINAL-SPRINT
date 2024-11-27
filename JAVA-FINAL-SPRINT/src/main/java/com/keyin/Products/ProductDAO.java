package com.keyin.Products;

import com.keyin.Database.DatabaseConfig;
import java.sql.*;


public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
    }