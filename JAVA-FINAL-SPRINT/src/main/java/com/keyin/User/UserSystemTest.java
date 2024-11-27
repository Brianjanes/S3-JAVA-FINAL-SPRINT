package com.keyin.User;

// this is just to test the DB user creation.
// to run multiple times you need to change the username i think.
// if you want to query run this in PGadmin:

//-- View all users
//SELECT * FROM users;
//
//-- View specific user
//SELECT * FROM users WHERE username = 'testuser';
//
//-- View roles
//SELECT DISTINCT role FROM users;


public class UserSystemTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        try {
            // Test registration
            User newUser = userService.registerUser("testuser", "password123", "test@example.com", "buyer");
            System.out.println("User registered: " + newUser.getUsername());

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