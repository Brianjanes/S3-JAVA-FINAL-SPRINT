-- Create enum for user roles
CREATE TYPE user_role AS ENUM ('buyer', 'seller', 'admin');

-- Users table with role
CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role user_role NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
                          product_id SERIAL PRIMARY KEY,
                          seller_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL,
                          quantity INTEGER NOT NULL CHECK (quantity >= 0),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        buyer_id INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order items table
CREATE TABLE order_items (
                             order_item_id SERIAL PRIMARY KEY,
                             order_id INTEGER REFERENCES orders(order_id) ON DELETE CASCADE,
                             product_id INTEGER REFERENCES products(product_id) ON DELETE SET NULL,
                             quantity INTEGER NOT NULL,
                             price_at_time DECIMAL(10,2) NOT NULL
);

-- Indexes for better query performance
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_orders_buyer ON orders(buyer_id);
CREATE INDEX idx_order_items_order ON order_items(order_id);

-- Sample data insertion
INSERT INTO users (username, password, email, role) VALUES
                                                        ('admin', '$2a$10$somehashedpassword', 'admin@example.com', 'admin'),
                                                        ('seller1', '$2a$10$somehashedpassword', 'seller1@example.com', 'seller'),
                                                        ('buyer1', '$2a$10$somehashedpassword', 'buyer1@example.com', 'buyer');