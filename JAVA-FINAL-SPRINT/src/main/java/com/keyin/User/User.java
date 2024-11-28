package com.keyin.User;

/**
 * Represents a user in the E-Commerce platform.
 * Users can be buyers, sellers, or administrators within the system,
 * with different permissions and capabilities based on their role.
 *
 * @author Kyle Hollett, Brad Ayers, Brian Janes
 * @version 1.0
 * @since 2024-11-27
 */

public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String role;

    /**
     * Constructs a User with all fields specified.
     *
     * @param user_id Unique identifier for the user
     * @param user_name Username for login
     * @param password Hashed password for authentication
     * @param email User's email address
     * @param role User's role (buyer/seller/admin)
     */
    public User(int user_id, String user_name, String password, String email, String role) {
        this.user_id = user_id;
        this.username = user_name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Constructs a User without an ID (typically used for new user registration).
     *
     * @param username Username for login
     * @param password Hashed password for authentication
     * @param email User's email address
     * @param role User's role (buyer/seller/admin)
     */
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the user's unique identifier.
     * @return The user ID
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Gets the username.
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the hashed password.
     * @return The hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's email address.
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's role.
     * @return The role (buyer/seller/admin)
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user's ID.
     * @param user_id The new user ID
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Sets the username.
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password (should be pre-hashed).
     * @param password The new hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the email address.
     * @param email The new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's role.
     * @param role The new role (buyer/seller/admin)
     */
    public void setRole(String role) {
        this.role = role;
    }
}