package com.horizon.ebooklibrary.service;

import com.horizon.ebooklibrary.model.LoginRequest;
import com.horizon.ebooklibrary.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit interface for authentication-related API calls.
 * This defines how the app communicates with the backend for login.
 */
public interface AuthService {

    /**
     * Sends login requests to the backend
     * @param request LoginRequest object containing email and password
     * @return The response with access token, refresh token, and role
     */
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
