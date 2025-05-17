package com.horizon.ebooklibrary.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides a configured Retrofit client for making HTTP requests.
 */
public class ApiClient {

    // Change this to the deployed backend URL when taking out of local development
    private static final String BASE_URL = "http://10.0.2.2:8080"; // 10.0.2.2 is Android Emulator's alias for localhost

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON <-> Java
                    .build();
        }
        return retrofit;
    }
}
