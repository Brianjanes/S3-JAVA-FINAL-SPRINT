package com.keyin.User;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String username, String password, String email, String role) {
        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(username, hashedPassword, email, role);
        try {
            return userDAO.createUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public User login(String username, String password) {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error during login", e);
        }
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
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user", e);
        }
    }
}