-- Drop tables if they exist to start fresh
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop enum type if it exists
DROP TYPE IF EXISTS user_role;

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
    ('seller2', '$2a$10$somehashedpassword', 'seller2@example.com', 'seller'),
    ('buyer1', '$2a$10$somehashedpassword', 'buyer1@example.com', 'buyer'),
    ('buyer2', '$2a$10$somehashedpassword', 'buyer2@example.com', 'buyer'),
    ('buyer3', '$2a$10$somehashedpassword', 'buyer3@example.com', 'buyer');

-- Insert sample products (20 products with random sellers)
INSERT INTO products (seller_id, name, description, price, quantity) VALUES
    (2, 'Laptop', 'A high-end gaming laptop', 1500.00, 5),
    (2, 'Smartphone', 'A smartphone with a powerful camera', 800.00, 10),
    (2, 'Headphones', 'Noise-canceling over-ear headphones', 150.00, 8),
    (2, 'Smartwatch', 'A smartwatch with fitness tracking', 200.00, 12),
    (2, 'Tablet', 'A 10-inch tablet for work and entertainment', 350.00, 15),
    (3, 'Coffee Maker', 'An automatic coffee machine', 100.00, 25),
    (3, 'Blender', 'A high-speed blender for smoothies', 80.00, 20),
    (3, 'Air Conditioner', 'Energy-efficient air conditioner', 500.00, 3),
    (3, 'Microwave', 'Compact microwave oven', 120.00, 18),
    (3, 'Electric Kettle', 'A fast-boiling electric kettle', 30.00, 30),
    (2, 'Monitor', 'A 27-inch 4K monitor', 300.00, 7),
    (2, 'Printer', 'A wireless printer', 120.00, 5),
    (2, 'Keyboard', 'Mechanical keyboard with RGB lighting', 100.00, 14),
    (2, 'Mouse', 'Wireless gaming mouse', 50.00, 22),
    (3, 'Camera', 'A professional DSLR camera', 1200.00, 4),
    (3, 'Projector', 'Portable HD projector', 350.00, 9),
    (3, 'Electric Scooter', 'A foldable electric scooter', 600.00, 6),
    (2, 'Power Bank', 'Portable power bank with fast charging', 40.00, 50),
    (3, 'Fan', 'A compact desk fan', 25.00, 40),
    (3, 'Heater', 'Small space heater', 60.00, 17);