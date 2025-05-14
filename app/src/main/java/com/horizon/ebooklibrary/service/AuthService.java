package com.horizon.ebooklibrary.service;

import com.horizon.ebooklibrary.model.LoginRequest;
import com.horizon.ebooklibrary.model.LoginResponse;
import com.horizon.ebooklibrary.model.SignupRequest;

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

    /**
     * Sends signup requests to the backend
     * @param request SignupRequest containing email and password
     * @return The backend returns 204 no content or 201 created so we use Call< Void >
     */
    @POST("/auth/signup")
    Call<Void> signup(@Body SignupRequest request);

}
