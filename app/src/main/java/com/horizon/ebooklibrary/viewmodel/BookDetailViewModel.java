package com.horizon.ebooklibrary.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.network.ApiClient;
import com.horizon.ebooklibrary.service.BookService;
import com.horizon.ebooklibrary.util.TokenManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for the Book Detail screen.
 * Handles marking a book as read or unread and exposes operation results to the UI.,
 */
public class BookDetailViewModel extends ViewModel {

    private final BookService bookService;
    private final MutableLiveData<String> operationMessage = new MutableLiveData<>();

    // For logging with logcat
    private static final String TAG = "BookDetailViewModel";

    public BookDetailViewModel() {
        this.bookService = ApiClient.getClient().create(BookService.class);
    }

    /**
     * Makes an API call to mark a specific chosen book as read for the authenticated user.
     * @param bookId the book ID passed through the request
     */
    public void markAsRead(long bookId) {
        String token = TokenManager.getInstance().getAccessToken();
        Log.d(TAG, "Marking book as Read. ID: " + bookId + ", Token Bearer " + token);

        bookService.markAsRead("Bearer " + token, bookId)
                .enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            operationMessage.postValue(response.body().get("message"));
                        } else {
                            operationMessage.postValue("Failed to mark as read");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                        Log.e(TAG, "markAsRead() network error", t);
                        operationMessage.postValue("Network error: " + t.getMessage());
                    }
                });
    }
    /**
     * Makes an API call to mark a specific chosen book as unread for the authenticated user.
     * @param bookId the book ID passed through the request
     */
    public void markAsUnread(long bookId) {
        String token = TokenManager.getInstance().getAccessToken();
        Log.d(TAG, "Marking book as Unread. ID: " + bookId + ", Token Bearer " + token);

        bookService.markAsUnread("Bearer " + token, bookId)
                .enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            operationMessage.postValue(response.body().get("message"));
                        } else {
                            operationMessage.postValue("Failed to mark as unread");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                        Log.e(TAG, "markAsUnread() network error", t);
                        operationMessage.postValue("Network error: " + t.getMessage());
                    }
                });
    }

    public LiveData<String> getOperationMessage() {
        return operationMessage;
    }
}
