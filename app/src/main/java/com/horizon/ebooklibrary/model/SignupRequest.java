package com.horizon.ebooklibrary.model;

/**
 * Represents the request body for user registration
 */
public class SignupRequest {

    private String email;
    private String password;

    public SignupRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
