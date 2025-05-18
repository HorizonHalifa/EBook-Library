package com.horizon.ebooklibrary.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.horizon.ebooklibrary.model.RefreshTokenRequest;
import com.horizon.ebooklibrary.model.RefreshTokenResponse;
import com.horizon.ebooklibrary.service.AuthService;
import com.horizon.ebooklibrary.util.TokenManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import retrofit2.Call;

/**
 * The class TokenAuthenticator is triggered automatically by OkHttp
 * whenever a request returns 'HTTP 401 Unauthorized'.
 * <p>
 * Responsibilities:
 * - Attempts to refresh the access token using the refresh token.
 * - Updates the stored access token if successful.
 * - Repeats the original request with the new token.
 * - Returns null if the token refresh fails (forces logout).
 */
public class TokenAuthenticator implements Authenticator {

    private static final String TAG = "TokenAuthenticator";
    private final AuthService authService;

    public TokenAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Request authenticate(@Nullable Route route, @NonNull okhttp3.Response response) throws IOException {
        Log.d(TAG, "Token expired. Attempting to refresh...");

        String refreshToken = TokenManager.getInstance().getRefreshToken();

        if (refreshToken == null) {
            // No refresh token available - user must log in again
            Log.d(TAG, "No refresh token available.");
            return null;
        }

        // prepare the token request
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);
        Call<RefreshTokenResponse> call = authService.refreshToken(refreshRequest);

        try {
            retrofit2.Response<RefreshTokenResponse> tokenResponse = call.execute();

            if (tokenResponse.body() != null && tokenResponse.isSuccessful()) {
                String newAccessToken = tokenResponse.body().getAccessToken();

                // Save the new access token
                Log.d(TAG, "Saving new access token" + newAccessToken);
                TokenManager.getInstance().saveAccessToken(newAccessToken);

                // Retry the original request with the new token
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();
            } else {
                // Token refresh failed, could be expired or invalid , make user log out
                Log.e(TAG, "Token refresh failed. Logging out.");
                TokenManager.getInstance().clearTokens();
                return null;
            }
        } catch (IOException e) {
            // If we get here, there is something wrong with the token
            Log.e(TAG, "Token refresh failed. Logging out.");
            return null;
        }
    }
}
