package com.keyin;

import com.keyin.User.*;
import com.keyin.Roles.*;
import com.keyin.Products.*;
import org.mindrot.jbcrypt.BCrypt;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EcommApp {
    private final UserService userService;
    private final ProductService productService;
    private final MultiWindowTextGUI gui;
    private User currentUser;
    private Window currentWindow;

    public EcommApp(UserService userService) throws IOException {
        this.userService = userService;
        this.productService = new ProductService();

        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
    }

    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            UserService userService = new UserService(userDAO);

            EcommApp app = new EcommApp(userService);
            app.start();
        } catch (IOException e) {
            System.err.println("Error initializing the application: " + e.getMessage());
        }
    }

    public void start() {
        displayMainMenu();
    }

    private void displayMainMenu() {
        // Close any existing window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("E-Commerce Platform");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Main Menu ==="));
        panel.addComponent(new Button("1. Login", this::handleLogin));
        panel.addComponent(new Button("2. Sign Up", this::handleSignUp));
        panel.addComponent(new Button("3. Exit", () -> System.exit(0)));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void handleLogin() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Login");
        currentWindow = window;
        Panel panel = new Panel(new GridLayout(2));

        Label usernameLabel = new Label("Username:");
        TextBox usernameBox = new TextBox();
        panel.addComponent(usernameLabel);
        panel.addComponent(usernameBox);

        Label passwordLabel = new Label("Password:");
        TextBox passwordBox = new TextBox().setMask('*');
        panel.addComponent(passwordLabel);
        panel.addComponent(passwordBox);

        panel.addComponent(new Button("Back", this::displayMainMenu));
        panel.addComponent(new Button("Submit", () -> {
            try {
                String username = usernameBox.getText();
                String password = passwordBox.getText();
                currentUser = userService.login(username, password);
                showRoleSpecificMenu();
            } catch (Exception e) {
                showErrorMessage("Login failed: " + e.getMessage());
            }
        }));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void handleSignUp() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Sign Up");
        currentWindow = window;
        Panel panel = new Panel(new GridLayout(2));

        Label usernameLabel = new Label("Username:");
        TextBox usernameBox = new TextBox();
        panel.addComponent(usernameLabel);
        panel.addComponent(usernameBox);

        Label passwordLabel = new Label("Password:");
        TextBox passwordBox = new TextBox().setMask('*');
        panel.addComponent(passwordLabel);
        panel.addComponent(passwordBox);

        Label emailLabel = new Label("Email:");
        TextBox emailBox = new TextBox();
        panel.addComponent(emailLabel);
        panel.addComponent(emailBox);

        Label roleLabel = new Label("Role (buyer/seller/admin):");
        TextBox roleBox = new TextBox();
        panel.addComponent(roleLabel);
        panel.addComponent(roleBox);

        panel.addComponent(new Button("Back", this::displayMainMenu));
        panel.addComponent(new Button("Submit", () -> {
            try {
                String username = usernameBox.getText();
                String password = passwordBox.getText();
                String email = emailBox.getText();
                String role = roleBox.getText();
                currentUser = userService.registerUser(username, password, email, role);
                showRoleSpecificMenu();
            } catch (Exception e) {
                showErrorMessage("Sign Up failed: " + e.getMessage());
            }
        }));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showRoleSpecificMenu() {
        switch (currentUser.getRole().toLowerCase()) {
            case "buyer" -> showBuyerMenu();
            case "seller" -> showSellerMenu();
            case "admin" -> showAdminMenu();
            default -> showErrorMessage("Invalid role: " + currentUser.getRole());
        }
    }

    private void showBuyerMenu() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Buyer Menu");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Buyer Menu ==="));
        panel.addComponent(new Button("Browse Products", this::displayAllProducts));
        panel.addComponent(new Button("Search Products", this::searchProducts));
        panel.addComponent(new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        }));
        panel.addComponent(new Button("Back", this::displayMainMenu));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showSellerMenu() {
        Window window = new BasicWindow("Seller Menu");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Seller Menu ==="));
        panel.addComponent(new Button("Add Product", this::addProduct));
        panel.addComponent(new Button("My Products", this::listSellerProducts));
        panel.addComponent(new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        }));
        panel.addComponent(new Button("Back", this::displayMainMenu));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void listSellerProducts() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("My Products");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== My Products ==="));
        panel.addComponent(new Button("Back", this::showSellerMenu));

        try {
            List<Product> sellerProducts = productService.getSellerProducts(currentUser);
            if (sellerProducts.isEmpty()) {
                panel.addComponent(new Label("You have no products listed."));
            } else {
                for (Product product : sellerProducts) {
                    Panel productPanel = new Panel(new GridLayout(2));
                    productPanel.addComponent(new Label(String.format("Name: %s | Price: $%.2f | Quantity: %d",
                            product.getName(), product.getPrice(), product.getQuantity())));
                    productPanel.addComponent(new Button("Edit", () -> this.productService.updateProduct(product, currentUser)));
                    productPanel.addComponent(new Button("Delete", () -> this.productService.deleteProduct(product.getProduct_id(), currentUser)));
                    panel.addComponent(productPanel);
                }
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving products: " + e.getMessage()));
        }

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showAdminMenu() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Admin Menu");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Admin Menu ==="));
        panel.addComponent(new Button("View All Users", this::viewAllUsers));
        panel.addComponent(new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        }));
        panel.addComponent(new Button("Back", this::displayMainMenu));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void displayAllProducts() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("All Products");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("Product Listing"));
        panel.addComponent(new Button("Back", this::showBuyerMenu));

        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                panel.addComponent(new Label("No products available."));
            } else {
                for (Product product : products) {
                    panel.addComponent(new Label(String.format("ID: %d | Name: %s | Price: $%.2f | Quantity: %d",
                            product.getProduct_id(), product.getName(), product.getPrice(), product.getQuantity())));
                }
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving products: " + e.getMessage()));
        }

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void searchProducts() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Search Products");
        currentWindow = window;
        Panel panel = new Panel(new GridLayout(2));

        Label searchLabel = new Label("Search Keyword:");
        TextBox searchBox = new TextBox();
        panel.addComponent(searchLabel);
        panel.addComponent(searchBox);

        Panel resultsPanel = new Panel();
        resultsPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Button("Search", () -> {
            try {
                String keyword = searchBox.getText();
                List<Product> products = productService.searchProducts(keyword);

                // Clear previous results
                resultsPanel.removeAllComponents();

                if (products.isEmpty()) {
                    resultsPanel.addComponent(new Label("No products found."));
                } else {
                    for (Product product : products) {
                        resultsPanel.addComponent(new Label(String.format("ID: %d | Name: %s | Price: $%.2f | Quantity: %d",
                                product.getProduct_id(), product.getName(), product.getPrice(), product.getQuantity())));
                    }
                }
            } catch (Exception e) {
                resultsPanel.removeAllComponents();
                resultsPanel.addComponent(new Label("Error searching products: " + e.getMessage()));
            }
        }));

        panel.addComponent(new Button("Back", this::showBuyerMenu));
        panel.addComponent(resultsPanel);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void addProduct() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Add Product");
        currentWindow = window;
        Panel panel = new Panel(new GridLayout(2));

        Label nameLabel = new Label("Product Name:");
        TextBox nameBox = new TextBox();
        panel.addComponent(nameLabel);
        panel.addComponent(nameBox);

        Label descLabel = new Label("Description:");
        TextBox descBox = new TextBox();
        panel.addComponent(descLabel);
        panel.addComponent(descBox);

        Label priceLabel = new Label("Price:");
        TextBox priceBox = new TextBox();
        panel.addComponent(priceLabel);
        panel.addComponent(priceBox);

        Label quantityLabel = new Label("Quantity:");
        TextBox quantityBox = new TextBox();
        panel.addComponent(quantityLabel);
        panel.addComponent(quantityBox);

        Label statusLabel = new Label("");
        panel.addComponent(statusLabel);

        panel.addComponent(new Button("Add Product", () -> {
            try {
                String name = nameBox.getText();
                String description = descBox.getText();
                double price = Double.parseDouble(priceBox.getText());
                int quantity = Integer.parseInt(quantityBox.getText());

                Product product = productService.createProduct(name, description, price, quantity, currentUser);
                statusLabel.setText("Product added successfully!");
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid price or quantity!");
            } catch (Exception e) {
                statusLabel.setText("Error adding product: " + e.getMessage());
            }
        }));

        panel.addComponent(new Button("Back", this::showSellerMenu));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void viewAllUsers() {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("All Users");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("View All Users"));
        panel.addComponent(new Button("Back", this::showAdminMenu));

        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                panel.addComponent(new Label("No users found."));
            } else {
                for (User user : users) {
                    panel.addComponent(new Label(String.format("ID: %d | Username: %s | Email: %s | Role: %s",
                            user.getUser_id(), user.getUsername(), user.getEmail(), user.getRole())));
                }
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving users: " + e.getMessage()));
        }

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showErrorMessage(String message) {
        // Close previous window
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Error");
        currentWindow = window;
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label(message));
        panel.addComponent(new Button("OK", this::displayMainMenu));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}