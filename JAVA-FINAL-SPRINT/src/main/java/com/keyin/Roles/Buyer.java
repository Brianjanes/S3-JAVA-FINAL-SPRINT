package com.keyin.Roles;

import com.keyin.User.User;

public class Buyer extends User {
    public Buyer(String username, String password, String email) {
        super(username, password, email, "BUYER");
    }
}
