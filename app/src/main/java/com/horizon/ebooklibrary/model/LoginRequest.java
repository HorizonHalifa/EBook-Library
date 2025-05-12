package com.horizon.ebooklibrary.model;

/**
 * Data model representing the login request sent to the backend.
 * This class is serialized to JSON automatically by Retrofit + Gson.
 */
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
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
