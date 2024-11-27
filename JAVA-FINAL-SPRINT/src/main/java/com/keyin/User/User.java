package com.keyin.User;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String role;

    // CONSTRUCTORS
    public User(int user_id, String user_name, String password, String email, String role) {
        this.user_id = user_id;
        this.username = user_name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // GETTERS
    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // SETTERS
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
