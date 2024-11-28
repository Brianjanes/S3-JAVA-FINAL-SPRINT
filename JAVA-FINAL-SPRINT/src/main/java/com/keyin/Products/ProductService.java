package com.keyin.Products;

import com.keyin.User.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class that handles all business logic for product operations.
 * This class acts as an intermediary between the API layer and data access layer,
 * ensuring all business rules and validations are enforced before any database operations.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class ProductService {
    private final ProductDAO productDAO;

    /**
     * Constructs a new ProductService and initializes the database connection.
     */
    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Creates a new product in the system.
     *
     * @param name        The name of the product (non-null, non-empty)
     * @param description A detailed description of the product (non-null, non-empty)
     * @param price       The price of the product (must be greater than 0)
     * @param quantity    The initial stock quantity (must be 0 or positive)
     * @param seller      The user creating the product (must have SELLER role)
     * @return The created Product object with generated ID
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If any validation fails or user is not a seller
     */
    public Product createProduct(String name, String description, double price,
                                 int quantity, User seller) throws SQLException {
        if (!"SELLER".equals(seller.getRole())) {
            throw new IllegalArgumentException("Only sellers can create products");
        }

        validateProductData(name, description, price, quantity);

        Product product = new Product(name, description, price, quantity, seller.getUser_id());
        return productDAO.createProduct(product);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The unique identifier of the product
     * @return The requested Product object
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If the product is not found
     */
    public Product getProduct(int productId) throws SQLException {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        return product;
    }

    /**
     * Updates an existing product's information.
     * Only the original seller can update their products.
     *
     * @param product The updated product information
     * @param seller The user attempting to update the product
     * @return true if update was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If the seller doesn't own the product or validation fails
     */
    public boolean updateProduct(Product product, User seller) throws SQLException {
        Product existingProduct = productDAO.getProductById(product.getProduct_id());
        if (existingProduct == null || existingProduct.getSeller_id() != seller.getUser_id()) {
            throw new IllegalArgumentException("You can only update your own products");
        }

        validateProductData(product.getName(), product.getDescription(),
                product.getPrice(), product.getQuantity());

        return productDAO.updateProduct(product);
    }

    /**
     * Deletes a product from the system.
     * Only the original seller can delete their products.
     *
     * @param productId The ID of the product to delete
     * @param seller The user attempting to delete the product
     * @return true if deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If the seller doesn't own the product
     */
    public boolean deleteProduct(int productId, User seller) throws SQLException {
        Product product = productDAO.getProductById(productId);
        if (product == null || product.getSeller_id() != seller.getUser_id()) {
            throw new IllegalArgumentException("You can only delete your own products");
        }
        return productDAO.deleteProduct(productId);
    }

    /**
     * Retrieves all products in the system.
     *
     * @return List of all products
     * @throws SQLException If a database error occurs
     */
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    /**
     * Retrieves all products for a specific seller.
     *
     * @param seller The seller whose products to retrieve
     * @return List of products belonging to the seller
     * @throws SQLException If a database error occurs
     */
    public List<Product> getSellerProducts(User seller) throws SQLException {
        return productDAO.getProductsBySeller(seller.getUser_id());
    }

    /**
     * Searches for products by keyword in name or description.
     *
     * @param keyword The search term to look for
     * @return List of products matching the search term
     * @throws SQLException If a database error occurs
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        return productDAO.searchProducts(keyword);
    }

    /**
     * Validates product data before creation or update.
     *
     * @param name Product name to validate
     * @param description Product description to validate
     * @param price Product price to validate
     * @param quantity Product quantity to validate
     * @throws IllegalArgumentException If any validation fails
     */
    private void validateProductData(String name, String description, double price, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    /**
     * Updates the quantity of an existing product.
     * Typically used during order processing or inventory management.
     *
     * @param productId The ID of the product to update
     * @param newQuantity The new quantity to set
     * @return true if update was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If product not found or quantity is negative
     */
    public boolean updateProductQuantity(int productId, int newQuantity) throws SQLException {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        product.setQuantity(newQuantity);
        return productDAO.updateProduct(product);
    }
}