package com.horizon.ebooklibrary.model;

/**
 * Sent in the request body to refresh an access token
 * @noinspection unused, unused
 */
public class RefreshTokenRequest {

    private String refreshToken;

    // Constructor
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setter
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
