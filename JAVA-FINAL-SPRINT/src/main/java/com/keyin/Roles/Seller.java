package com.keyin.Roles;

import com.keyin.User.User;

public class Seller extends User {
    public Seller(String username, String password, String email) {
        super(username, password, email, "SELLER");
    }
}