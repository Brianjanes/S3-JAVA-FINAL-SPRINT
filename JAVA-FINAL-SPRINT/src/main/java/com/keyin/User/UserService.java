package com.keyin.User;

import com.keyin.Roles.*;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Service class for user-related business logic and authentication.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.1
 * @since 2024-11-27
 */
public class UserService {
    private final UserDAO userDAO;
    private static final int BCRYPT_WORKLOAD = 12;

    /**
     * Constructs UserService with data access object.
     * @param userDAO Data access object for user operations
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user with individual fields.
     *
     * @param username Username for new user
     * @param password Plain text password
     * @param email User's email
     * @param role User's role
     * @return Registered user
     * @throws IllegalArgumentException if validation fails
     */
    public User registerUser(String username, String password, String email, String role) {
        validateInputFields(username, password, email, role);
        String hashedPassword = hashPassword(password);

        User user = switch (role.toLowerCase()) {
            case "buyer" -> new Buyer(username, hashedPassword, email);
            case "seller" -> new Seller(username, hashedPassword, email);
            case "admin" -> new Admin(username, hashedPassword, email);
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };

        try {
            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null) {
                throw new IllegalArgumentException("Username already exists");
            }
            return userDAO.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Database error during registration: " + e.getMessage());
        }
    }

    /**
     * Authenticates user login attempt.
     *
     * @param username Username attempting to login
     * @param password Plain text password to verify
     * @return Authenticated user if successful
     * @throws IllegalArgumentException if credentials invalid
     * @throws RuntimeException if database error occurs
     */
    public User login(String username, String password) {
        try {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Username and password cannot be empty");
            }

            User user = userDAO.getUserByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            boolean matches = BCrypt.checkpw(password, user.getPassword());
            if (matches) {
                return convertToRoleSpecificUser(user);
            }
            throw new IllegalArgumentException("Invalid password");
        } catch (SQLException e) {
            throw new RuntimeException("Database error during login: " + e.getMessage());
        }
    }

    /**
     * Retrieves all users from database, sorted by ID in ascending order.
     *
     * @return List of all users sorted by ID
     * @throws RuntimeException if database error occurs
     */
    public List<User> getAllUsers() {
        try {
            // Sort users by ID in ascending order
            return userDAO.getAllUsers().stream()
                    .sorted(Comparator.comparingInt(User::getUser_id))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage());
        }
    }

    /**
     * Deletes user by ID.
     *
     * @param userId ID of user to delete
     * @return true if deletion successful
     * @throws RuntimeException if database error occurs
     */
    public boolean deleteUser(int userId) {
        try {
            if (userDAO.getUserById(userId) == null) {
                throw new IllegalArgumentException("User not found: " + userId);
            }
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Retrieves user by ID.
     *
     * @param userId ID of user to retrieve
     * @return User if found
     * @throws IllegalArgumentException if user not found
     * @throws RuntimeException if database error occurs
     */
    public User getUserById(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + userId);
            }
            return convertToRoleSpecificUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user: " + e.getMessage());
        }
    }

    /**
     * Validates user data before registration.
     *
     * @param user User to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUserData(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateInputFields(user.getUsername(), user.getPassword(),
                user.getEmail(), user.getRole());
    }

    /**
     * Validates individual user fields.
     *
     * @param username Username to validate
     * @param password Password to validate
     * @param email Email to validate
     * @param role Role to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateInputFields(String username, String password, String email, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (role == null || !role.toLowerCase().matches("^(buyer|seller|admin)$")) {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param plainTextPassword Password to hash
     * @return BCrypt hashed password
     */
    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(BCRYPT_WORKLOAD));
    }

    /**
     * Converts generic User to role-specific subclass.
     *
     * @param user Generic user to convert
     * @return Role-specific user instance
     */
    private User convertToRoleSpecificUser(User user) {
        return switch (user.getRole().toLowerCase()) {
            case "buyer" -> new Buyer(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            case "seller" -> new Seller(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            case "admin" -> new Admin(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            default -> user;
        };
    }

    /**
     * Updates a specific field of a user.
     *
     * @param userId ID of the user to update
     * @param fieldName Name of the field to update
     * @param newValue New value for the field
     * @return true if update was successful
     * @throws Exception if an error occurs during update
     */
    public boolean updateUserField(int userId, String fieldName, String newValue) throws Exception {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        switch (fieldName.toLowerCase()) {
            case "username" -> user.setUsername(newValue);
            case "password" -> user.setPassword(hashPassword(newValue)); // Hash the password
            case "email" -> {
                if (!newValue.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new IllegalArgumentException("Invalid email format");
                }
                user.setEmail(newValue);
            }
            case "role" -> {
                if (!newValue.matches("^(buyer|seller|admin)$")) {
                    throw new IllegalArgumentException("Invalid role");
                }
                user.setRole(newValue);
            }
            default -> throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        return userDAO.updateUser(user);
    }
}