package com.keyin.Products;

public class Product {
    private int product_id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int seller_id;

    // Constructor
    public Product(int product_id, String name, String description, double price, int quantity, int seller_id) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.seller_id = seller_id;
    }

    // Constructor without ID (for new products)
    public Product(String name, String description, double price, int quantity, int seller_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.seller_id = seller_id;
    }

    // Getters
    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSeller_id() {
        return seller_id;
    }

    // Setters
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }
}