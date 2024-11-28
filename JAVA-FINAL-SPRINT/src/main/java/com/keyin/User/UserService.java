package com.keyin.User;

import com.keyin.Roles.*;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(User user) {
        // Hash the password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        try {
            return userDAO.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public User registerUser(String username, String password, String email, String role) {
        User user = switch (role.toUpperCase()) {
            case "BUYER" -> new Buyer(username, password, email);
            case "SELLER" -> new Seller(username, password, email);
            case "ADMIN" -> new Admin(username, password, email);
            default -> new Buyer(username, password, email);
        };
        return registerUser(user);
    }

    public User login(String username, String password) {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                // Convert to specific role type
                return convertToRoleSpecificUser(user);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    private User convertToRoleSpecificUser(User user) {
        return switch (user.getRole()) {
            case "BUYER" -> new Buyer(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            case "SELLER" -> new Seller(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            case "ADMIN" -> new Admin(user.getUser_id(), user.getUsername(),
                    user.getPassword(), user.getEmail());
            default -> user;
        };
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users", e);
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public User getUserById(int userId) {
        try {
            User user = userDAO.getUserById(userId);
            return user != null ? convertToRoleSpecificUser(user) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user", e);
        }
    }
}