package com.horizon.ebooklibrary.service;

import com.horizon.ebooklibrary.model.Book;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Retrofit interface for book-related operations.
 * Includes admin-only upload functionality for new books.
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

}
