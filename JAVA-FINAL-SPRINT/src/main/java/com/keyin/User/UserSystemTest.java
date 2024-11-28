package com.keyin.User;

import com.keyin.Roles.*;

public class UserSystemTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        try {
            // Test registration
            User newUser = new Buyer("testuser", "password123", "test@example.com");
            User registeredUser = userService.registerUser(newUser);
            System.out.println("User registered: " + registeredUser.getUsername());

            // Test login
            User loggedInUser = userService.login("testuser", "password123");
            System.out.println("Login successful: " + (loggedInUser != null));

            // Test user retrieval
            System.out.println("All users: " + userService.getAllUsers().size());
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}