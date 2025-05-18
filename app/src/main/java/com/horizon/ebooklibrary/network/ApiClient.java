package com.horizon.ebooklibrary.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.horizon.ebooklibrary.service.AuthService;
import com.horizon.ebooklibrary.util.TokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Provides a configured Retrofit client with:
 * - Authorization header for access tokens.
 * - TokenAuthenticator for auto-refresh on 401 'Unauthorized' error handling.
 */
public class ApiClient {

    // Change this to the deployed backend URL when taking out of local development
    private static final String BASE_URL = "http://10.0.2.2:8080"; // 10.0.2.2 is Android Emulator's alias for localhost

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null) {

            // Step 1: Create a temporary Retrofit to get AuthService
            Retrofit tempRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON <-> Java
                    .build();


            AuthService authService = tempRetrofit.create(AuthService.class);
            TokenAuthenticator tokenAuthenticator = new TokenAuthenticator(authService);

            /*
             * Step 2: Create OkHttpClient with:
             * - Authorization header injection
             * - Token refresh handling
             */
            OkHttpClient client = new OkHttpClient.Builder()
                    .authenticator(tokenAuthenticator)
                    .addInterceptor(chain -> {
                      String token = TokenManager.getInstance().getAccessToken();
                      if(token != null) {
                          return chain.proceed(chain.request().newBuilder()
                                  .addHeader("Authorization", "Bearer " + token)
                                  .build());
                      } else {
                          return chain.proceed(chain.request());
                      }
                    })
                    .build();

            // Step 3: Create Retrofit using the OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
