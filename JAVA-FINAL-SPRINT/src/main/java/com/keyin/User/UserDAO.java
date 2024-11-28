package com.keyin.User;

import com.keyin.Database.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing User entities in the database.
 * Handles all database operations related to users including CRUD operations.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */
public class UserDAO {
    private Connection connection;

    /**
     * Initializes the UserDAO with a database connection.
     * @throws RuntimeException if database connection fails
     */
    public UserDAO() {
        try {
            this.connection = DatabaseConfig.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Creates a new user in the database.
     *
     * @param user User object containing user information
     * @return User object with assigned ID if successful, null otherwise
     * @throws SQLException if database operation fails
     */
    public User createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?::user_role) RETURNING user_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setUser_id(rs.getInt("user_id"));
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId ID of the user to retrieve
     * @return User object if found, null otherwise
     * @throws SQLException if database operation fails
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        }
        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username Username to search for
     * @return User object if found, null otherwise
     * @throws SQLException if database operation fails
     */
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all users
     * @throws SQLException if database operation fails
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
        }
        return users;
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId ID of the user to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates user information in the database.
     *
     * @param user User object containing updated information
     * @return true if update was successful, false otherwise
     * @throws SQLException if database operation fails
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role = ?::user_role WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getUser_id());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Prints all users to console for debugging purposes.
     *
     * @throws SQLException if database operation fails
     */
    public void printAllUsers() throws SQLException {
        List<User> users = getAllUsers();
        users.forEach(u -> System.out.println(
                "ID: " + u.getUser_id() +
                        ", Name: " + u.getUsername() +
                        ", Role: " + u.getRole()
        ));
    }
}