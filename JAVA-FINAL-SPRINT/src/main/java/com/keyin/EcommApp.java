package com.keyin;

import com.keyin.User.*;
import com.keyin.Roles.*;
import com.keyin.Products.*;
import org.mindrot.jbcrypt.BCrypt;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Help.Ansi;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Main application class for the E-Commerce platform using picocli.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
@Command(
        name = "ecommerce-app",
        description = "E-Commerce Platform CLI Application",
        mixinStandardHelpOptions = true
)
public class EcommApp implements Callable<Integer> {

    private final Scanner scanner;
    private final UserService userService;
    private final ProductService productService;
    private User currentUser;

    public EcommApp() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService(new UserDAO());
        this.productService = new ProductService();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new EcommApp()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
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
                    return 0;
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
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            currentUser = userService.login(username, password);
            System.out.println("Login successful!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("System error: " + e.getMessage());
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
        String role = switch (roleChoice) {
            case 1 -> "buyer";
            case 2 -> "seller";
            case 3 -> "admin";
            default -> {
                System.out.println("Invalid role selection!");
                yield null;
            }
        };

        if (role != null) {
            try {
                currentUser = userService.registerUser(username, password, email, role);
                System.out.println("Registration successful!");
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Registration failed: " + e.getMessage());
            }
        }
        return false;
    }

    private void showRoleSpecificMenu() {
        switch (currentUser.getRole().toLowerCase()) {
            case "buyer" -> showBuyerMenu();
            case "seller" -> showSellerMenu();
            case "admin" -> showAdminMenu();
            default -> System.out.println("Invalid role: " + currentUser.getRole());
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
                System.out.println("\n=== Product Details ===");
                System.out.println("Name: " + product.getName());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Quantity: " + product.getQuantity());
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
                System.out.println(user.getUsername() + " (" + user.getRole() + ")");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Enter user_id to delete: ");
            int userid = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            if (userService.deleteUser(userid)){
                System.out.println("User deleted successfully!");
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
                System.out.println(product.getName() + " by " + seller.getUsername());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found!");
        } else {
            for (Product product : products) {
                System.out.println(product.getName() + " - $" + product.getPrice());
            }
        }
    }

    private int getInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // invalid input
        }
    }
}