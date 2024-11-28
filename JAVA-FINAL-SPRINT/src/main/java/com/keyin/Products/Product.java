package com.keyin.Products;

/**
 * Represents a product in the e-commerce system.
 * This class contains all the basic information about a product including
 * its identifier, name, description, price, quantity, and seller information.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class Product {
    private int product_id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int seller_id;

    /**
     * Constructs a new Product without an ID (typically used when creating a new product).
     *
     * @param name The name of the product
     * @param description A detailed description of the product
     * @param price The price of the product (must be greater than 0)
     * @param quantity The available quantity (must be 0 or positive)
     * @param seller_id The ID of the seller who owns this product
     * @throws IllegalArgumentException if price is 0 or negative, or if quantity is negative
     */
    public Product(String name, String description, double price, int quantity, int seller_id) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.seller_id = seller_id;
    }

    /**
     * Constructs a Product with an existing ID (typically used when loading from database).
     *
     * @param product_id The unique identifier of the product
     * @param name The name of the product
     * @param description A detailed description of the product
     * @param price The price of the product (must be greater than 0)
     * @param quantity The available quantity (must be 0 or positive)
     * @param seller_id The ID of the seller who owns this product
     * @throws IllegalArgumentException if price is 0 or negative, or if quantity is negative
     */
    public Product(int product_id, String name, String description, double price, int quantity, int seller_id) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.seller_id = seller_id;
    }

    /**
     * Gets the unique identifier of the product.
     *
     * @return The product's ID
     */
    public int getProduct_id() {
        return product_id;
    }

    /**
     * Gets the name of the product.
     *
     * @return The product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the product.
     *
     * @return The product's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the price of the product.
     *
     * @return The product's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the available quantity of the product.
     *
     * @return The product's quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the ID of the seller who owns this product.
     *
     * @return The seller's ID
     */
    public int getSeller_id() {
        return seller_id;
    }

    /**
     * Sets the product's ID. Typically used when a new product is saved to the database.
     *
     * @param product_id The unique identifier to set
     */
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    /**
     * Sets the product's name.
     *
     * @param name The new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product's description.
     *
     * @param description The new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the product's price.
     *
     * @param price The new price to set (should be validated before setting)
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the product's available quantity.
     *
     * @param quantity The new quantity to set (should be validated before setting)
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the ID of the seller who owns this product.
     *
     * @param seller_id The new seller ID to set
     */
    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }
}