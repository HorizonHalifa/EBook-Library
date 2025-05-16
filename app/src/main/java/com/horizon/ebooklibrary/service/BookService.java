package com.horizon.ebooklibrary.service;

import com.horizon.ebooklibrary.model.Book;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;

/**
 * Retrofit interface for book-related operations.
 * Includes:
 * admin-only upload functionality for new books.
 * Authenticated read/unread book fetching per user.
 */
public interface BookService {

    /**
     * Uploads a new book to the backend. Admin only.
     * @param authHeader Bearer token for authorization
     * @param title Book title (text field)
     * @param author Author name (text field)
     * @param description Description of the book (text field)
     * @param coverImage Book's cover image (JPG or PNG)
     * @param pdfFile The PDF file of the book
     * @return Call object for the resulting Book
     */
    @Multipart
    @POST("/books/upload")
    Call<Book> uploadBook(
            @Header("Authorization") String authHeader,
            @Part("title") RequestBody title,
            @Part("author") RequestBody author,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part pdfFile
    );

    /**
     * Fetches a list of books the current user has marked as read.
     * @param authHeader JWT Bearer token
     * @return List of all read books for the authenticated user
     */
    @GET("/books/read")
    Call<List<Book>> getReadBooks(@Header("Authorization") String authHeader);

    /**
     * Fetches a list of books the current user has marked as un-read or yet to have marked as read
     * @param authHeader JWT Bearer token
     * @return Lists of all unread books for the authenticated user
     */
    @GET("/books/unread")
    Call<List<Book>> getUnreadBooks(@Header("Authorization") String authHeader);

    /**
     * Marks a book as read for the authenticated user.
     */
    @PUT("/books/{id}/mark-read")
    Call<Map<String, String>> markAsRead(@Header("Authorization") String authHeader, @Path("id") long bookId);

    /**
     * Marks a book as unread for the authenticated user.
     */
    @PUT("/books/{id}/mark-unread")
    Call<Map<String, String>> markAsUnread(@Header("Authorization") String authHeader, @Path("id") long bookId);

}
