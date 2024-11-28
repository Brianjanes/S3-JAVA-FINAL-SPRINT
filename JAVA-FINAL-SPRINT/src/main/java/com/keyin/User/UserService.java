package com.keyin.User;

import com.keyin.Roles.*;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.List;

/**
 * Service class that handles user-related business logic and authentication.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class UserService {
    private final UserDAO userDAO;

    /**
     * Constructs UserService with data access object.
     * @param userDAO Data access object for user operations
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user with pre-constructed User object.
     *
     * @param user User object containing registration information
     * @return Registered user with ID if successful, null otherwise
     * @throws RuntimeException if database operation fails
     */
    public User registerUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        try {
            return userDAO.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    /**
     * Overloaded method to register user with individual fields.
     *
     * @param username Username for new user
     * @param password Plain text password
     * @param email User's email
     * @param role User's role (buyer/seller/admin)
     * @return Registered user if successful, null otherwise
     */
    public User registerUser(String username, String password, String email, String role) {
        User user = switch (role.toLowerCase()) {
            case "buyer" -> new Buyer(username, password, email);
            case "seller" -> new Seller(username, password, email);
            case "admin" -> new Admin(username, password, email);
            default -> new Buyer(username, password, email);
        };
        return registerUser(user);
    }

    /**
     * Authenticates user login attempt.
     *
     * @param username Username attempting to login
     * @param password Plain text password to verify
     * @return Authenticated user if successful, null otherwise
     * @throws RuntimeException if database operation fails
     */
    public User login(String username, String password) {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return convertToRoleSpecificUser(user);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    /**
     * Converts generic User to role-specific subclass.
     *
     * @param user Generic user object to convert
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
     * Retrieves all users from database.
     *
     * @return List of all users
     * @throws RuntimeException if database operation fails
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users", e);
        }
    }

    /**
     * Deletes user by ID.
     *
     * @param userId ID of user to delete
     * @return true if deletion successful, false otherwise
     * @throws RuntimeException if database operation fails
     */
    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    /**
     * Retrieves user by ID and converts to role-specific type.
     *
     * @param userId ID of user to retrieve
     * @return Role-specific user if found, null otherwise
     * @throws RuntimeException if database operation fails
     */
    public User getUserById(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            return user != null ? convertToRoleSpecificUser(user) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user", e);
        }
    }
}