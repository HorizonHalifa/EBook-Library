package com.horizon.ebooklibrary.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.horizon.ebooklibrary.model.LoginRequest;
import com.horizon.ebooklibrary.model.LoginResponse;
import com.horizon.ebooklibrary.network.ApiClient;
import com.horizon.ebooklibrary.service.AuthService;
import com.horizon.ebooklibrary.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository responsible for handing authentication-related operations.
 * Connects the ViewModel to the network layer and token storage.
 */
public class AuthRepository {

    private final AuthService authService;


    public AuthRepository() {
        authService = ApiClient.getClient().create(AuthService.class);
    }

    /**
     * Triggers the login API call and notifies the callback.
     * @param request Login request with email and password
     * @param callback Callback to receive success for failure result
     */
    public void login(LoginRequest request, LoginCallback callback) {
        authService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    // Save tokens via TokenManager
                    TokenManager.getInstance().saveLoginData(response.body());

                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Invalid credentials");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e("AuthRepository", "Login failed: ", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }


    /**
     * Callback interface to notify ViewModel about login result.
     */
    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String error);
    }
}
