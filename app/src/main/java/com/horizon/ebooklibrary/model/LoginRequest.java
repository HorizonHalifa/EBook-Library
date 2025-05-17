package com.horizon.ebooklibrary.model;

/**
 * Data model representing the login request sent to the backend.
 * This class is serialized to JSON automatically by Retrofit + Gson.
 * @noinspection unused
 */
public class LoginRequest {

    private final String email;
    private final String password;

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
