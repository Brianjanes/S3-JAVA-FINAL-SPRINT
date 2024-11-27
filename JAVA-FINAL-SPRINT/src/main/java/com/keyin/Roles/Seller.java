package com.keyin.Roles;

import com.keyin.User.User;

public class Seller extends User {
    public Seller(int user_id, String username, String password, String email) {
        super(user_id, username, password, email, "SELLER");
    }

    public Seller(String username, String password, String email) {
        super(username, password, email, "SELLER");
    }
}