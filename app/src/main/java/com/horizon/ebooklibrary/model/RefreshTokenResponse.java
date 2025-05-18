package com.horizon.ebooklibrary.model;

/**
 * This class represents the response from the backend after refreshing a token
 */
public class RefreshTokenResponse {

    private String accessToken;

    // Getter
    public String getAccessToken() {
        return accessToken;
    }

    // Setter
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
