package com.keyin.Roles;

import com.keyin.User.User;

public class Admin extends User {
    public Admin(int user_id, String username, String password, String email) {
        super(user_id, username, password, email, "ADMIN");
    }

    public Admin(String username, String password, String email) {
        super(username, password, email, "ADMIN");
    }
}