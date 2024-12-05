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

    public EcommApp(UserService userService) throws IOException {
        this.userService = userService;
        this.productService = new ProductService();

        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
    }

    public static void main(String[] args) {
        try {
            // Create required services
            UserDAO userDAO = new UserDAO();
            UserService userService = new UserService(userDAO);

            // Create and start application
            EcommApp app = new EcommApp(userService);
            app.start();
        } catch (IOException e) {
            System.err.println("Error initializing the application: " + e.getMessage());
        }
    }

    public void start() {
        while (true) {
            displayMainMenu();
        }
    }

    private void displayMainMenu() {
        Window window = new BasicWindow("E-Commerce Platform");
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
        Window window = new BasicWindow("Login");
        Panel panel = new Panel(new GridLayout(2));

        Label usernameLabel = new Label("Username:");
        TextBox usernameBox = new TextBox();
        panel.addComponent(usernameLabel);
        panel.addComponent(usernameBox);

        Label passwordLabel = new Label("Password:");
        TextBox passwordBox = new TextBox().setMask('*');
        panel.addComponent(passwordLabel);
        panel.addComponent(passwordBox);

        panel.addComponent(new EmptySpace()); // Empty space for alignment
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
        Window window = new BasicWindow("Sign Up");
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

        panel.addComponent(new EmptySpace());
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
        Window window = new BasicWindow("Buyer Menu");
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Buyer Menu ==="));
        panel.addComponent(new Button("Browse Products", this::displayAllProducts));
        panel.addComponent(new Button("Search Products", this::searchProducts));
        panel.addComponent(new Button("Logout", () -> currentUser = null));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showSellerMenu() {
        Window window = new BasicWindow("Seller Menu");
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Seller Menu ==="));
        panel.addComponent(new Button("Add Product", this::addProduct));
        panel.addComponent(new Button("Logout", () -> currentUser = null));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void showAdminMenu() {
        Window window = new BasicWindow("Admin Menu");
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("=== Admin Menu ==="));
        panel.addComponent(new Button("View All Users", this::viewAllUsers));
        panel.addComponent(new Button("Logout", () -> currentUser = null));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    private void displayAllProducts() {
        // TODO: Implement product listing logic
        showErrorMessage("Display all products functionality is under development.");
    }

    private void searchProducts() {
        // TODO: Implement search logic
        showErrorMessage("Search products functionality is under development.");
    }

    private void addProduct() {
        // TODO: Implement add product logic
        showErrorMessage("Add product functionality is under development.");
    }

    private void viewAllUsers() {
        // TODO: Implement view all users logic
        showErrorMessage("View all users functionality is under development.");
    }

    private void showErrorMessage(String message) {
        Window window = new BasicWindow("Error");
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label(message));
        panel.addComponent(new Button("OK", window::close));

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
}
