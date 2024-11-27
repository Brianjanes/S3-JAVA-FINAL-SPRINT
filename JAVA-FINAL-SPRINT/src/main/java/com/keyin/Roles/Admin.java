package com.keyin.Roles;

import com.keyin.User.User;

public class Admin extends User {
    public Admin(String username, String password, String email) {
        super(username, password, email, "ADMIN");
    }
}