package com.horizon.ebooklibrary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.network.ApiClient;
import com.horizon.ebooklibrary.service.BookService;
import com.horizon.ebooklibrary.util.TokenManager;

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

    public BookDetailViewModel() {
        bookService = ApiClient.getClient().create(BookService.class);
    }

    /**
     * Makes an API call to mark a specific chosen book as read for the authenticated user.
     * @param bookId the book ID passed through the request
     */
    public void markAsRead(long bookId) {
        String token = TokenManager.getInstance().getAccessToken();
        bookService.markAsRead("Bearer " + token, bookId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        operationMessage.postValue(response.isSuccessful() ? "Marked as Read" : "Failed to mark as read");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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

        bookService.markAsUnread("Bearer " + token, bookId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        operationMessage.postValue(response.isSuccessful() ? "Marked as Unread" : "Failed to mark as unread");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    public LiveData<String> getOperationMessage() {
        return operationMessage;
    }
}
