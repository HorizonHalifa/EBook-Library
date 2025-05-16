package com.horizon.ebooklibrary.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.horizon.ebooklibrary.model.Book;
import com.horizon.ebooklibrary.network.ApiClient;
import com.horizon.ebooklibrary.service.BookService;
import com.horizon.ebooklibrary.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository that manages all book related API calls.
 * Provides methods to fetch read/unread book for authenticated user.
 */
public class BookRepository {

    private final BookService bookService;

    public BookRepository() {
        this.bookService = ApiClient.getClient().create(BookService.class);
    }

    /**
     * Fetches the list of books marked as read by the current user.
     * @param callback Callback to return the result or an error
     */
    public void getReadBooks(BookCallback callback) {
        String token = TokenManager.getInstance().getAccessToken();
        String authHeader = "Bearer " + token;

        bookService.getReadBooks(authHeader).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch read books. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("BookRepository", "getReadBooks failed", t);
                callback.onError("Network error " + t.getMessage());
            }
        });

    }

    /**
     * Fetches the list of unread books of the current user.
     * @param callback Callback to return the result or an error
     */
    public void getUnreadBooks(BookCallback callback) {
        String token = TokenManager.getInstance().getAccessToken();
        String authHeader = "Bearer " + token;

        bookService.getUnreadBooks(authHeader).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if(response.body() != null && response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch unread books. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Log.e("BookRepository", "getUnreadBooks failed", t);
                callback.onError("Network error " + t.getMessage());
            }
        });
    }

    /**
     * Callback interface for delivering book list results to the ViewModel
     */
    public interface BookCallback {
        void onSuccess(List<Book> books);
        void onError(String errorMessage);
    }

}

