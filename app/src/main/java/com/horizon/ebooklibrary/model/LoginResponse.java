package com.horizon.ebooklibrary.model;

/**
 * Data model representing the login response from the backend.
 * This class is deserialized automatically by Retrofit + Gson.
 */
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String role;

    // No-args constructor required for Gson
    public LoginResponse() {}

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getRole() {
        return role;
    }
}
