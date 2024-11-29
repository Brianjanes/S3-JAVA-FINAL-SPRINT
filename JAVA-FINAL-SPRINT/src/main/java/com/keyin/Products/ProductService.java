package com.keyin.Products;

import com.keyin.User.User;
import java.sql.SQLException;
import java.util.List;
/**test
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
     * @param seller      The user creating the product (must have seller role)
     * @return The created Product object with generated ID
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If validation fails or user is not a seller
     */
    public Product createProduct(String name, String description, double price,
                                 int quantity, User seller) {
        try {
            validateSellerRole(seller);
            validateProductData(name, description, price, quantity);

            Product product = new Product(name, description, price, quantity, seller.getUser_id());
            return productDAO.createProduct(product);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while creating product: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product data: " + e.getMessage());
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The unique identifier of the product
     * @return The requested Product object
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If the product is not found
     */
    public Product getProduct(int productId) {
        try {
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found with ID: " + productId);
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching product: " + e.getMessage());
        }
    }

    /**
     * Updates an existing product's information.
     * Only the original seller can update their products.
     *
     * @param product The updated product information
     * @param seller The user attempting to update the product
     * @return true if update was successful
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If seller doesn't own the product or validation fails
     */
    public boolean updateProduct(Product product, User seller) {
        try {
            validateProductOwnership(product, seller);
            validateProductData(product.getName(), product.getDescription(),
                    product.getPrice(), product.getQuantity());

            return productDAO.updateProduct(product);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating product: " + e.getMessage());
        }
    }

    /**
     * Deletes a product from the system.
     * Only the original seller can delete their products.
     *
     * @param productId The ID of the product to delete
     * @param seller The user attempting to delete the product
     * @return true if deletion was successful
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If seller doesn't own the product
     */
    public boolean deleteProduct(int productId, User seller) {
        try {
            Product product = productDAO.getProductById(productId);
            validateProductOwnership(product, seller);

            return productDAO.deleteProduct(productId);
        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting product: " + e.getMessage());
        }
    }

    /**
     * Retrieves all products in the system.
     *
     * @return List of all products
     * @throws RuntimeException If database operation fails
     */
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching all products: " + e.getMessage());
        }
    }

    /**
     * Retrieves all products for a specific seller.
     *
     * @param seller The seller whose products to retrieve
     * @return List of products belonging to the seller
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If user is not a seller
     */
    public List<Product> getSellerProducts(User seller) {
        try {
            validateSellerRole(seller);
            return productDAO.getProductsBySeller(seller.getUser_id());
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching seller products: " + e.getMessage());
        }
    }

    /**
     * Searches for products by keyword in name or description.
     *
     * @param keyword The search term to look for
     * @return List of products matching the search term
     * @throws RuntimeException If database operation fails
     * @throws IllegalArgumentException If search keyword is empty
     */
    public List<Product> searchProducts(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("Search keyword cannot be empty");
            }
            return productDAO.searchProducts(keyword.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Database error while searching products: " + e.getMessage());
        }
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
     * Validates that a user has seller role.
     *
     * @param seller The user to validate
     * @throws IllegalArgumentException If user is null or not a seller
     */
    private void validateSellerRole(User seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }
        if (!"seller".equalsIgnoreCase(seller.getRole())) {
            throw new IllegalArgumentException("User must be a seller to perform this action");
        }
    }

    /**
     * Validates that a product exists and belongs to the seller.
     *
     * @param product The product to validate
     * @param seller The seller who should own the product
     * @throws IllegalArgumentException If product not found or doesn't belong to seller
     */
    private void validateProductOwnership(Product product, User seller) {
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (product.getSeller_id() != seller.getUser_id()) {
            throw new IllegalArgumentException("You can only modify your own products");
        }
    }
}