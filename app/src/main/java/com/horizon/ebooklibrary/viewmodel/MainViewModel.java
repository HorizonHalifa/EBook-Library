package com.horizon.ebooklibrary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.model.Book;
import com.horizon.ebooklibrary.repository.BookRepository;

import java.util.List;

/**
 * ViewModel for MainActivity, handles loading read/unread books from the backend and exposes them to the UI components using LiveData
 */
public class MainViewModel extends ViewModel {

    private final BookRepository bookRepository;

    private final MutableLiveData<List<Book>> readBooks = new MutableLiveData<>();
    private final MutableLiveData<List<Book>> unreadBooks = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MainViewModel() {
        this.bookRepository = new BookRepository();
    }

    /**
     * Starts the async loading of read and unread books from the backend
     * (The separate retrofit calls are synchronized)
     */
    public void loadBooks() {
        loadReadBooks();
        loadUnreadBooks();
    }

    /**
     * Loads the marked-as-read books from the logic in bookRepository and posts them as a list.
     */
    private void loadReadBooks() {
        bookRepository.getReadBooks(new BookRepository.BookCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                readBooks.postValue(books);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }

    /**
     * Loads the marked-as-unread books from the logic in bookRepository and posts them as a list.
     */
    private void loadUnreadBooks() {
        bookRepository.getUnreadBooks(new BookRepository.BookCallback() {
            @Override
            public void onSuccess(List<Book> books) {
                unreadBooks.postValue(books);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        });
    }


    /**
     * Exposes read books to the UI.
     */
    public LiveData<List<Book>> getReadBooks() {
        return readBooks;
    }

    /**
     * Exposes unread books to the UI.
     */
    public LiveData<List<Book>> getUnreadBooks() {
        return unreadBooks;
    }

    /**
     * Exposes error messages to the UI.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
