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

/**
 * Main application class for the E-Commerce platform with JLine integration.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.1
 * @since 2024-11-27
 */

public class EcommApp {
    private final UserService userService;
    private final ProductService productService;
    private final MultiWindowTextGUI gui;
    private User currentUser;
    private Window currentWindow;

    // Increased terminal size for better visibility
    private static final TerminalSize LARGE_WINDOW_SIZE = new TerminalSize(120, 40);
    private static final TerminalSize MEDIUM_WINDOW_SIZE = new TerminalSize(100, 30);

    public EcommApp(UserService userService) throws IOException {
        this.userService = userService;
        this.productService = new ProductService();

        // Create screen with larger size
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(LARGE_WINDOW_SIZE);
        Screen screen = terminalFactory.createScreen();
        screen.startScreen();

        // Initialize GUI with more spacing
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
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("E-Commerce Platform");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        // Create panel with vertical layout and padding
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        // Add padded components
        panel.addComponent(new Label("=== Main Menu ===")
                .setLayoutData(GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER
                )));


        Button loginButton = new Button("1. Login", this::handleLogin);
        loginButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(loginButton);

        Button signupButton = new Button("2. Sign Up", this::handleSignUp);
        signupButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(signupButton);

        Button exitButton = new Button("3. Exit", () -> System.exit(0));
        exitButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(exitButton);

        window.setComponent(panel);

        // Set window size
        window.setSize(MEDIUM_WINDOW_SIZE);

        gui.addWindowAndWait(window);
    }

    private void handleLogin() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Login");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        // Create grid layout with increased padding
        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        // Username input with padding
        Label usernameLabel = new Label("Username:");
        usernameLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox usernameBox = new TextBox(new TerminalSize(30, 1));
        usernameBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(usernameLabel);
        panel.addComponent(usernameBox);

        // Password input with padding
        Label passwordLabel = new Label("Password:");
        passwordLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox passwordBox = new TextBox(new TerminalSize(30, 1)).setMask('*');
        passwordBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(passwordLabel);
        panel.addComponent(passwordBox);

        // Back button with padding
        Button backButton = new Button("Back", this::displayMainMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        // Submit button with padding
        Button submitButton = new Button("Submit", () -> {
            try {
                String username = usernameBox.getText();
                String password = passwordBox.getText();
                currentUser = userService.login(username, password);
                showRoleSpecificMenu();
            } catch (Exception e) {
                showErrorMessage("Login failed: " + e.getMessage());
            }
        });
        submitButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(submitButton);

        window.setComponent(panel);

        // Set window size
        window.setSize(MEDIUM_WINDOW_SIZE);

        gui.addWindowAndWait(window);
    }

    private void handleSignUp() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Sign Up");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        // Create grid layout with increased padding
        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        // Username input with padding
        Label usernameLabel = new Label("Username:");
        usernameLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox usernameBox = new TextBox(new TerminalSize(30, 1));
        usernameBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(usernameLabel);
        panel.addComponent(usernameBox);

        // Password input with padding
        Label passwordLabel = new Label("Password:");
        passwordLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox passwordBox = new TextBox(new TerminalSize(30, 1)).setMask('*');
        passwordBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(passwordLabel);
        panel.addComponent(passwordBox);

        // Email input with padding
        Label emailLabel = new Label("Email:");
        emailLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox emailBox = new TextBox(new TerminalSize(30, 1));
        emailBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(emailLabel);
        panel.addComponent(emailBox);

        // Role input with padding
        Label roleLabel = new Label("Role (buyer/seller/admin):");
        roleLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox roleBox = new TextBox(new TerminalSize(30, 1));
        roleBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(roleLabel);
        panel.addComponent(roleBox);

        // Back button with padding
        Button backButton = new Button("Back", this::displayMainMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        // Submit button with padding
        Button submitButton = new Button("Submit", () -> {
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
        });
        submitButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(submitButton);

        window.setComponent(panel);

        // Set window size
        window.setSize(LARGE_WINDOW_SIZE);

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
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Buyer Menu");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        panel.addComponent(new Label("=== Buyer Menu ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        Button browseButton = new Button("Browse Products", this::displayAllProducts);
        browseButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(browseButton);

        Button searchButton = new Button("Search Products", this::searchProducts);
        searchButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(searchButton);

        Button logoutButton = new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        });
        logoutButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(logoutButton);

        Button backButton = new Button("Back", this::displayMainMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        window.setSize(MEDIUM_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }

    private void showSellerMenu() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Seller Menu");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        panel.addComponent(new Label("=== Seller Menu ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        Button addProductButton = new Button("Add Product", this::addProduct);
        addProductButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(addProductButton);

        Button myProductsButton = new Button("My Products", this::listSellerProducts);
        myProductsButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(myProductsButton);

        Button logoutButton = new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        });
        logoutButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(logoutButton);

        Button backButton = new Button("Back", this::displayMainMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        window.setSize(MEDIUM_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }

    private void listSellerProducts() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("My Products");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        mainPanel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        mainPanel.addComponent(new Label("=== My Products ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        // Status label for showing success/error messages
        Label statusLabel = new Label("");
        statusLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        mainPanel.addComponent(statusLabel);

        try {
            List<Product> sellerProducts = productService.getSellerProducts(currentUser);
            if (sellerProducts.isEmpty()) {
                Label noProductsLabel = new Label("You have no products listed.");
                noProductsLabel.setLayoutData(GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        1,
                        1
                ));
                mainPanel.addComponent(noProductsLabel);
            } else {
                for (Product product : sellerProducts) {
                    Panel productPanel = new Panel(new GridLayout(3));
                    productPanel.setLayoutData(
                            GridLayout.createLayoutData(
                                    GridLayout.Alignment.CENTER,
                                    GridLayout.Alignment.CENTER,
                                    true,
                                    false,
                                    1,
                                    1
                            )
                    );

                    Label productLabel = new Label(String.format("Name: %s | Price: $%.2f | Quantity: %d",
                            product.getName(), product.getPrice(), product.getQuantity()));
                    productLabel.setLayoutData(GridLayout.createLayoutData(
                            GridLayout.Alignment.FILL,
                            GridLayout.Alignment.CENTER,
                            true,
                            false,
                            1,
                            1
                    ));
                    productPanel.addComponent(productLabel);

                    Button editButton = new Button("Edit", () -> {
                        // Create a new window for editing the product
                        Window editWindow = new BasicWindow("Edit Product");
                        editWindow.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));

                        Panel editPanel = new Panel(new GridLayout(2));
                        editPanel.setLayoutData(
                                GridLayout.createLayoutData(
                                        GridLayout.Alignment.CENTER,
                                        GridLayout.Alignment.CENTER,
                                        true,
                                        false,
                                        5,
                                        3
                                )
                        );

                        // Name input
                        editPanel.addComponent(new Label("Name:"));
                        TextBox nameInput = new TextBox(product.getName());
                        editPanel.addComponent(nameInput);

                        // Description input
                        editPanel.addComponent(new Label("Description:"));
                        TextBox descInput = new TextBox(product.getDescription());
                        editPanel.addComponent(descInput);

                        // Price input
                        editPanel.addComponent(new Label("Price:"));
                        TextBox priceInput = new TextBox(String.format("%.2f", product.getPrice()));
                        editPanel.addComponent(priceInput);

                        // Quantity input
                        editPanel.addComponent(new Label("Quantity:"));
                        TextBox quantityInput = new TextBox(String.valueOf(product.getQuantity()));
                        editPanel.addComponent(quantityInput);

                        // Save button
                        Button saveButton = new Button("Save", () -> {
                            try {
                                // Validate and parse inputs
                                String newName = nameInput.getText().trim();
                                String newDescription = descInput.getText().trim();
                                double newPrice = Double.parseDouble(priceInput.getText().trim());
                                int newQuantity = Integer.parseInt(quantityInput.getText().trim());

                                // Create updated product object
                                Product updatedProduct = new Product(
                                        product.getProduct_id(),
                                        newName,
                                        newDescription,
                                        newPrice,
                                        newQuantity,
                                        product.getSeller_id()
                                );

                                // Attempt to update the product
                                boolean updateSuccess = productService.updateProduct(updatedProduct, currentUser);

                                if (updateSuccess) {
                                    // Update successful, refresh the list and close edit window
                                    statusLabel.setText("Product updated successfully!");
                                    editWindow.close();
                                    listSellerProducts(); // Refresh the products list
                                } else {
                                    statusLabel.setText("Failed to update product.");
                                }
                            } catch (NumberFormatException e) {
                                statusLabel.setText("Invalid number format. Please check price and quantity.");
                            } catch (IllegalArgumentException e) {
                                statusLabel.setText(e.getMessage());
                            }
                        });
                        editPanel.addComponent(saveButton);

                        // Cancel button
                        Button cancelButton = new Button("Cancel", editWindow::close);
                        editPanel.addComponent(cancelButton);

                        editWindow.setComponent(editPanel);
                        editWindow.setSize(LARGE_WINDOW_SIZE);
                        gui.addWindowAndWait(editWindow);
                    });
                    editButton.setLayoutData(GridLayout.createLayoutData(
                            GridLayout.Alignment.CENTER,
                            GridLayout.Alignment.CENTER,
                            false,
                            false,
                            1,
                            1
                    ));
                    productPanel.addComponent(editButton);

                    Button deleteButton = new Button("Delete", () -> {
                        try {
                            boolean deleteSuccess = this.productService.deleteProduct(product.getProduct_id(), currentUser);
                            if (deleteSuccess) {
                                statusLabel.setText("Product deleted successfully!");
                                listSellerProducts(); // Refresh the products list
                            } else {
                                statusLabel.setText("Failed to delete product.");
                            }
                        } catch (Exception e) {
                            statusLabel.setText("Error deleting product: " + e.getMessage());
                        }
                    });
                    deleteButton.setLayoutData(GridLayout.createLayoutData(
                            GridLayout.Alignment.CENTER,
                            GridLayout.Alignment.CENTER,
                            false,
                            false,
                            1,
                            1
                    ));
                    productPanel.addComponent(deleteButton);

                    mainPanel.addComponent(productPanel);
                }
            }

            Button backButton = new Button("Back", this::showSellerMenu);
            backButton.setLayoutData(GridLayout.createLayoutData(
                    GridLayout.Alignment.CENTER,
                    GridLayout.Alignment.CENTER,
                    true,
                    false,
                    1,
                    1
            ));
            mainPanel.addComponent(backButton);
        } catch (Exception e) {
            Label errorLabel = new Label("Error retrieving products: " + e.getMessage());
            errorLabel.setLayoutData(GridLayout.createLayoutData(
                    GridLayout.Alignment.CENTER,
                    GridLayout.Alignment.CENTER,
                    true,
                    false,
                    1,
                    1
            ));
            mainPanel.addComponent(errorLabel);

            Button backButton = new Button("Back", this::showSellerMenu);
            backButton.setLayoutData(GridLayout.createLayoutData(
                    GridLayout.Alignment.CENTER,
                    GridLayout.Alignment.CENTER,
                    true,
                    false,
                    1,
                    1
            ));
            mainPanel.addComponent(backButton);
        }

        window.setComponent(mainPanel);
        window.setSize(LARGE_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }

    private void showAdminMenu() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Admin Menu");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        panel.addComponent(new Label("=== Admin Menu ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        Button viewUsersButton = new Button("View All Users", this::viewAllUsers);
        viewUsersButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(viewUsersButton);

        Button viewProductsButton = new Button("View Products with Seller Details", this::viewAllProductsWithSellers);
        viewProductsButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(viewProductsButton);

        Button updateUserButton = new Button("Update User", this::updateUserMenu);
        updateUserButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(updateUserButton);

        Button deleteUserButton = new Button("Delete User", this::deleteUser);
        deleteUserButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(deleteUserButton);

        Button logoutButton = new Button("Logout", () -> {
            currentUser = null;
            displayMainMenu();
        });
        logoutButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(logoutButton);

        Button backButton = new Button("Back", this::displayMainMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        window.setSize(MEDIUM_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }

    private void updateUserMenu() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Update User");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        // User ID input with padding
        Label userIdLabel = new Label("User ID:");
        userIdLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox userIdBox = new TextBox(new TerminalSize(30, 1));
        userIdBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(userIdLabel);
        panel.addComponent(userIdBox);

        // Field to Update input with padding
        Label fieldLabel = new Label("Field to Update (username/password/email/role):");
        fieldLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox fieldBox = new TextBox(new TerminalSize(30, 1));
        fieldBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(fieldLabel);
        panel.addComponent(fieldBox);

        // New Value input with padding
        Label valueLabel = new Label("New Value:");
        valueLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.END,
                GridLayout.Alignment.CENTER,
                false,
                false,
                1,
                1
        ));
        TextBox valueBox = new TextBox(new TerminalSize(30, 1));
        valueBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(valueLabel);
        panel.addComponent(valueBox);

        // Status label
        Label statusLabel = new Label("");
        statusLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                2,
                1
        ));
        panel.addComponent(statusLabel);

        // Submit button with padding
        Button submitButton = new Button("Submit", () -> {
            try {
                int userId = Integer.parseInt(userIdBox.getText());
                String field = fieldBox.getText();
                String newValue = valueBox.getText();

                boolean success = userService.updateUserField(userId, field, newValue);
                if (success) {
                    statusLabel.setText("User updated successfully!");
                } else {
                    statusLabel.setText("Failed to update user. Check inputs.");
                }
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid User ID!");
            } catch (Exception e) {
                statusLabel.setText("Error: " + e.getMessage());
            }
        });
        submitButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(submitButton);

        // Back button with padding
        Button backButton = new Button("Back", this::showAdminMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        window.setSize(MEDIUM_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }

    private void displayAllProducts() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("All Products");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 5, 3
        ));

        panel.addComponent(new Label("Product Listing")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                panel.addComponent(new Label("No products available."));
            } else {
                for (Product product : products) {
                    panel.addComponent(new Label(String.format(
                            "ID: %d | Name: %s | Price: $%.2f | Quantity: %d",
                            product.getProduct_id(), product.getName(), product.getPrice(), product.getQuantity()
                    )));
                }
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving products: " + e.getMessage()));
        }

        Button backButton = new Button("Back", this::showBuyerMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 1, 1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void searchProducts() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Search Products");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 5, 3
        ));

        Label searchLabel = new Label("Search Keyword:");
        TextBox searchBox = new TextBox();
        panel.addComponent(searchLabel);
        panel.addComponent(searchBox);

        Panel resultsPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        resultsPanel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.CENTER,
                true, true
        ));

        Button searchButton = new Button("Search", () -> {
            try {
                String keyword = searchBox.getText();
                List<Product> products = productService.searchProducts(keyword);

                // Clear previous results
                resultsPanel.removeAllComponents();

                if (products.isEmpty()) {
                    resultsPanel.addComponent(new Label("No products found."));
                } else {
                    for (Product product : products) {
                        resultsPanel.addComponent(new Label(String.format(
                                "ID: %d | Name: %s | Price: $%.2f | Quantity: %d",
                                product.getProduct_id(), product.getName(), product.getPrice(), product.getQuantity()
                        )));
                    }
                }
            } catch (Exception e) {
                resultsPanel.removeAllComponents();
                resultsPanel.addComponent(new Label("Error searching products: " + e.getMessage()));
            }
        });

        Button backButton = new Button("Back", this::showBuyerMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 1, 1
        ));

        panel.addComponent(searchButton);
        panel.addComponent(backButton);
        panel.addComponent(resultsPanel);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void addProduct() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Add Product");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 5, 3
        ));

        panel.addComponent(new Label("Product Name:"));
        TextBox nameBox = new TextBox(new TerminalSize(30, 1));
        panel.addComponent(nameBox);

        panel.addComponent(new Label("Description:"));
        TextBox descBox = new TextBox(new TerminalSize(30, 1));
        panel.addComponent(descBox);

        panel.addComponent(new Label("Price:"));
        TextBox priceBox = new TextBox(new TerminalSize(30, 1));
        panel.addComponent(priceBox);

        panel.addComponent(new Label("Quantity:"));
        TextBox quantityBox = new TextBox(new TerminalSize(30, 1));
        panel.addComponent(quantityBox);

        Label statusLabel = new Label("");
        statusLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 2, 1
        ));
        panel.addComponent(statusLabel);

        Button addButton = new Button("Add Product", () -> {
            try {
                String name = nameBox.getText();
                String description = descBox.getText();
                double price = Double.parseDouble(priceBox.getText());
                int quantity = Integer.parseInt(quantityBox.getText());

                productService.createProduct(name, description, price, quantity, currentUser);
                statusLabel.setText("Product added successfully!");
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid price or quantity!");
            } catch (Exception e) {
                statusLabel.setText("Error adding product: " + e.getMessage());
            }
        });

        Button backButton = new Button("Back", this::showSellerMenu);

        panel.addComponent(addButton);
        panel.addComponent(backButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void viewAllUsers() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("All Users");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        panel.addComponent(new Label("=== View All Users ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                panel.addComponent(new Label("No users found.")
                        .setLayoutData(GridLayout.createLayoutData(
                                GridLayout.Alignment.CENTER,
                                GridLayout.Alignment.CENTER,
                                true,
                                false
                        )));
            } else {
                for (User user : users) {
                    panel.addComponent(new Label(String.format("ID: %d | Username: %s | Email: %s | Role: %s",
                            user.getUser_id(), user.getUsername(), user.getEmail(), user.getRole()))
                            .setLayoutData(GridLayout.createLayoutData(
                                    GridLayout.Alignment.FILL,
                                    GridLayout.Alignment.CENTER,
                                    true,
                                    false
                            )));
                }
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving users: " + e.getMessage())
                    .setLayoutData(GridLayout.createLayoutData(
                            GridLayout.Alignment.CENTER,
                            GridLayout.Alignment.CENTER,
                            true,
                            false
                    )));
        }

        Button backButton = new Button("Back", this::showAdminMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void viewAllProductsWithSellers() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("All Products with Sellers");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true, false, 5, 3
                )
        );

        panel.addComponent(new Label("=== Products with Seller Details ===")
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        try {
            List<Product> products = productService.getAllProducts();
            for (Product product : products) {
                User seller = userService.getUserById(product.getSeller_id());
                String sellerInfo = seller != null
                        ? String.format("Seller: %s (Email: %s)", seller.getUsername(), seller.getEmail())
                        : "Seller: Unknown";

                panel.addComponent(new Label(String.format(
                        "Product ID: %d | Name: %s | Price: $%.2f | Quantity: %d | %s",
                        product.getProduct_id(), product.getName(), product.getPrice(), product.getQuantity(), sellerInfo
                )));
            }
        } catch (Exception e) {
            panel.addComponent(new Label("Error retrieving products: " + e.getMessage()));
        }

        Button backButton = new Button("Back", this::showAdminMenu);
        backButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 1, 1
        ));
        panel.addComponent(backButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void deleteUser() {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Delete User");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel(new GridLayout(2));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true, false, 5, 3
                )
        );

        panel.addComponent(new Label("Enter User ID:"));
        TextBox userIdBox = new TextBox(new TerminalSize(30, 1));
        panel.addComponent(userIdBox);

        Label statusLabel = new Label("");
        statusLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true, false, 2, 1
        ));
        panel.addComponent(statusLabel);

        Button deleteButton = new Button("Delete", () -> {
            try {
                int userId = Integer.parseInt(userIdBox.getText());
                boolean success = userService.deleteUser(userId);

                if (success) {
                    statusLabel.setText("User deleted successfully!");
                } else {
                    statusLabel.setText("Failed to delete user. User ID may not exist.");
                }
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid User ID format!");
            } catch (Exception e) {
                statusLabel.setText("Error: " + e.getMessage());
            }
        });

        Button backButton = new Button("Back", this::showAdminMenu);

        panel.addComponent(deleteButton);
        panel.addComponent(backButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }


    private void showErrorMessage(String message) {
        if (currentWindow != null) {
            currentWindow.close();
        }

        Window window = new BasicWindow("Error");
        window.setHints(List.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));
        currentWindow = window;

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setLayoutData(
                GridLayout.createLayoutData(
                        GridLayout.Alignment.CENTER,
                        GridLayout.Alignment.CENTER,
                        true,
                        false,
                        5,
                        3
                )
        );

        Label errorLabel = new Label(message);
        errorLabel.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(errorLabel);

        Button okButton = new Button("OK", this::displayMainMenu);
        okButton.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER,
                true,
                false,
                1,
                1
        ));
        panel.addComponent(okButton);

        window.setComponent(panel);
        window.setSize(MEDIUM_WINDOW_SIZE);
        gui.addWindowAndWait(window);
    }
}