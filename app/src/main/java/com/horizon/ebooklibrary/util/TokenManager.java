package com.horizon.ebooklibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.horizon.ebooklibrary.model.LoginResponse;

/**
 * Helper utility class for storing and retrieving authentication tokens and user role.
 */
public class TokenManager {

    private static TokenManager instance;
    private SharedPreferences prefs;

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ROLE = "user_role";

    private TokenManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
    }

    /**
     * Initializes the TokenManager
     * @param context Application's context
     */
    public static void init(Context context) {
        if(instance == null) {
            instance = new TokenManager((context));
        }
    }

    /**
     * @return instance if it has already been initialized.
     */
    public static TokenManager getInstance() {
        if(instance == null) {
            throw new IllegalStateException("TokenManager is not initialized. Call init(context) first.");
        }
        return instance;
    }

    /**
     * Saves the login response tokens and role to SharedPreferences.
     * @param response The LoginResponse object from the server.
     */
    public void saveLoginData(LoginResponse response) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, response.getAccessToken());
        editor.putString(KEY_REFRESH_TOKEN, response.getRefreshToken());
        editor.putString(KEY_USER_ROLE, response.getRole());
        editor.apply();
    }

    /**
     * @return saved access token.
     */
    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    /**
     * @return the saved refresh token.
     */
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    /**
     * @return the saved user role ("USER" or "ADMIN")
     */
    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, null);
    }

    /**
     * Clears all stored authentication data.
     */
    public void clearTokens() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.remove(KEY_USER_ROLE);
        editor.apply();
    }
}
