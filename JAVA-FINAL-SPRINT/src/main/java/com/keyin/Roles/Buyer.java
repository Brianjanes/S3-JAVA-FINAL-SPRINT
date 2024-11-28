package com.keyin.Roles;

import com.keyin.User.User;

public class Buyer extends User {
    public Buyer(int user_id, String username, String password, String email) {
        super(user_id, username, password, email, "BUYER");
    }

    public Buyer(String username, String password, String email) {
        super(username, password, email, "buyer");
    }}