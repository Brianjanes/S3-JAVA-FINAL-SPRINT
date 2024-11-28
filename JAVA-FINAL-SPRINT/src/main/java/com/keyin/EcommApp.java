package com.keyin;

import com.keyin.User.*;
import com.keyin.Roles.*;
import com.keyin.Products.*;
import java.util.Scanner;
import java.util.List;

/**
 * Main application class for the E-Commerce platform.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class EcommApp {
    // Main method - entry point of the program
    public static void main(String[] args) {
        // Create required services
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        // Create and start application
        EcommApp app = new EcommApp(userService);
        app.start();
    }

    private final Scanner scanner;
    private final UserService userService;
    private final ProductService productService;
    private User currentUser;

    public EcommApp(UserService userService) {
        this.scanner = new Scanner(System.in);
        this.userService = userService;
        this.productService = new ProductService();
    }

    public void start() {
        while (true) {
            displayMainMenu();
            int choice = getInput();

            switch (choice) {
                case 1 -> {
                    if (handleLogin()) {
                        showRoleSpecificMenu();
                    }
                }
                case 2 -> {
                    if (handleSignUp()) {
                        showRoleSpecificMenu();
                    }
                }
                case 3 -> {
                    System.out.println("Thank you for using our E-Commerce platform!");
                    return;
                }
                default -> System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== E-Commerce Platform ===");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private boolean handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userService.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials!");
            return false;
        }
    }

    private boolean handleSignUp() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.println("\nSelect your role:");
        System.out.println("1. Buyer");
        System.out.println("2. Seller");
        System.out.println("3. Admin");
        System.out.print("Choose role: ");

        int roleChoice = getInput();
        User newUser = switch (roleChoice) {
            case 1 -> new Buyer(username, password, email);
            case 2 -> new Seller(username, password, email);
            case 3 -> new Admin(username, password, email);
            default -> {
                System.out.println("Invalid role selection!");
                yield null;
            }
        };

        if (newUser != null) {
            currentUser = userService.registerUser(newUser);
            if (currentUser != null) {
                System.out.println("Registration successful!");
                return true;
            }
        }
        System.out.println("Registration failed!");
        return false;
    }

    private void showRoleSpecificMenu() {
        switch (currentUser.getRole()) {
            case "buyer" -> showBuyerMenu();
            case "seller" -> showSellerMenu();
            case "admin" -> showAdminMenu();
            default -> System.out.println("Invalid role!");
        }
    }

    private void showBuyerMenu() {
        while (true) {
            System.out.println("\n=== Buyer Menu ===");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Products");
            System.out.println("3. View Product Details");
            System.out.println("4. Logout");

            int choice = getInput();
            switch (choice) {
                case 1 -> displayAllProducts();
                case 2 -> searchProducts();
                case 3 -> viewProductDetails();
                case 4 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void showSellerMenu() {
        while (true) {
            System.out.println("\n=== Seller Menu ===");
            System.out.println("1. Add Product");
            System.out.println("2. View My Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Logout");

            int choice = getInput();
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> viewMyProducts();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. View All Products");
            System.out.println("4. Logout");

            int choice = getInput();
            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> deleteUser();
                case 3 -> viewAllProductsWithSellers();
                case 4 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addProduct() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();

            System.out.print("Enter product description: ");
            String description = scanner.nextLine();

            System.out.print("Enter price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            Product product = productService.createProduct(name, description, price, quantity, currentUser);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyProducts() {
        try {
            List<Product> products = productService.getSellerProducts(currentUser);
            displayProducts(products);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        try {
            System.out.print("Enter product ID to update: ");
            int productId = Integer.parseInt(scanner.nextLine());

            Product product = productService.getProduct(productId);
            if (product != null && product.getSeller_id() == currentUser.getUser_id()) {
                System.out.print("Enter new name (or press enter to skip): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) product.setName(name);

                System.out.print("Enter new description (or press enter to skip): ");
                String description = scanner.nextLine();
                if (!description.isEmpty()) product.setDescription(description);

                System.out.print("Enter new price (or press enter to skip): ");
                String priceStr = scanner.nextLine();
                if (!priceStr.isEmpty()) product.setPrice(Double.parseDouble(priceStr));

                System.out.print("Enter new quantity (or press enter to skip): ");
                String quantityStr = scanner.nextLine();
                if (!quantityStr.isEmpty()) product.setQuantity(Integer.parseInt(quantityStr));

                if (productService.updateProduct(product, currentUser)) {
                    System.out.println("Product updated successfully!");
                }
            } else {
                System.out.println("Product not found or you don't have permission to update it.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            System.out.print("Enter product ID to delete: ");
            int productId = Integer.parseInt(scanner.nextLine());

            if (productService.deleteProduct(productId, currentUser)) {
                System.out.println("Product deleted successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            displayProducts(products);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchProducts() {
        try {
            System.out.print("Enter search keyword: ");
            String keyword = scanner.nextLine();
            List<Product> products = productService.searchProducts(keyword);
            displayProducts(products);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewProductDetails() {
        try {
            System.out.print("Enter product ID: ");
            int productId = Integer.parseInt(scanner.nextLine());
            Product product = productService.getProduct(productId);
            if (product != null) {
                displayProductDetails(product);
            } else {
                System.out.println("Product not found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                System.out.printf("ID: %d | Username: %s | Email: %s | Role: %s%n",
                        user.getUser_id(), user.getUsername(), user.getEmail(), user.getRole());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Enter user ID to delete: ");
            int userId = Integer.parseInt(scanner.nextLine());
            if (userService.deleteUser(userId)) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllProductsWithSellers() {
        try {
            List<Product> products = productService.getAllProducts();
            for (Product product : products) {
                User seller = userService.getUserById(product.getSeller_id());
                System.out.printf("Product ID: %d | Name: %s | Price: $%.2f | Quantity: %d%n" +
                                "Description: %s%n" +
                                "Seller: %s | Email: %s%n%n",
                        product.getProduct_id(), product.getName(), product.getPrice(),
                        product.getQuantity(), product.getDescription(),
                        seller.getUsername(), seller.getEmail());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        for (Product product : products) {
            System.out.printf("ID: %d | Name: %s | Price: $%.2f | Quantity: %d%n" +
                            "Description: %s%n%n",
                    product.getProduct_id(), product.getName(), product.getPrice(),
                    product.getQuantity(), product.getDescription());
        }
    }

    private void displayProductDetails(Product product) {
        System.out.printf("Product Details:%n" +
                        "ID: %d%n" +
                        "Name: %s%n" +
                        "Description: %s%n" +
                        "Price: $%.2f%n" +
                        "Quantity: %d%n",
                product.getProduct_id(), product.getName(), product.getDescription(),
                product.getPrice(), product.getQuantity());
    }

    private int getInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}