package com.keyin.Products;

import com.keyin.Database.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for handling all database operations related to Products.
 * This class provides an abstraction layer between the application and the database,
 * implementing CRUD (Create, Read, Update, Delete) operations for products.
 *
 * @author @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class ProductDAO {
    private Connection connection;

    /**
     * Constructs a new ProductDAO and establishes a database connection.
     *
     * @throws RuntimeException if database connection fails
     */
    public ProductDAO() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Creates a new product in the database.
     *
     * @param product The product object containing all product information except ID
     * @return The same product object with its database-generated ID, or null if creation fails
     * @throws SQLException if a database error occurs
     */
    public Product createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, quantity, seller_id) VALUES (?, ?, ?, ?, ?) RETURNING product_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setInt(5, product.getSeller_id());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product.setProduct_id(rs.getInt("product_id"));
                return product;
            }
        }
        return null;
    }

    /**
     * Retrieves a product from the database by its ID.
     *
     * @param productId The unique identifier of the product
     * @return The Product object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("seller_id")
                );
            }
        }
        return null;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products in the database
     * @throws SQLException if a database error occurs
     */
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("seller_id")
                ));
            }
        }
        return products;
    }

    /**
     * Retrieves all products from a specific seller.
     *
     * @param sellerId The unique identifier of the seller
     * @return List of products belonging to the specified seller
     * @throws SQLException if a database error occurs
     */
    public List<Product> getProductsBySeller(int sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE seller_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("seller_id")
                ));
            }
        }
        return products;
    }

    /**
     * Searches for products based on a keyword in their name or description.
     * The search is case-insensitive and matches partial words.
     *
     * @param keyword The search term to look for in product names and descriptions
     * @return List of products matching the search criteria
     * @throws SQLException if a database error occurs
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name ILIKE ? OR description ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("seller_id")
                ));
            }
        }
        return products;
    }

    /**
     * Updates an existing product's information in the database.
     *
     * @param product The product object containing updated information
     * @return true if the product was successfully updated, false if no product was found
     * @throws SQLException if a database error occurs
     */
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ? WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setInt(5, product.getProduct_id());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param productId The unique identifier of the product to delete
     * @return true if the product was successfully deleted, false if no product was found
     * @throws SQLException if a database error occurs
     */
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }
}