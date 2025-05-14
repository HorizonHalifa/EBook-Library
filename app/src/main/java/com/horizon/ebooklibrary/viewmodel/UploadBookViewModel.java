package com.horizon.ebooklibrary.viewmodel;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.horizon.ebooklibrary.model.Book;
import com.horizon.ebooklibrary.network.ApiClient;
import com.horizon.ebooklibrary.service.BookService;
import com.horizon.ebooklibrary.util.TokenManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for handling the logic and backend interaction related to admin book uploads.
 */
public class UploadBookViewModel extends ViewModel {

    private final BookService bookService;

    private final MutableLiveData<Boolean> uploadSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UploadBookViewModel() {
        bookService = ApiClient.getClient().create(BookService.class);
    }

    /**
     * Uploads a book by validating all inputs and sending them to the backend as a multipart/form-data request.
     */
    public void uploadBook(Context context, String title, String author, String description, Uri coverUri, Uri pdfUri) {

        // Validate fields
        if(!validateFields(title, author, description, coverUri, pdfUri)) {
            return; // Stop here if the input is invalid.
        }

        try {

            // Convert files
            MultipartBody.Part coverPart = createMultipartFromUri(context, coverUri, "coverImage");
            MultipartBody.Part pdfPart = createMultipartFromUri(context, pdfUri, "pdfFile");

            RequestBody titlePart = RequestBody.create(MultipartBody.FORM, title);
            RequestBody authorPart = RequestBody.create(MultipartBody.FORM, author);
            RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);

            String token = TokenManager.getInstance().getAccessToken();
            String authHeader = "Bearer " + token;

            bookService.uploadBook(authHeader, titlePart, authorPart, descriptionPart, coverPart, pdfPart)
                    .enqueue(new Callback<Book>() {
                        @Override
                        public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                            if(response.isSuccessful()) {
                                uploadSuccess.postValue(true);
                            } else {
                                errorMessage.postValue("Upload failed: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                            errorMessage.postValue("Network error: " + t.getMessage());
                        }
                    });

        } catch(IOException e) {
            errorMessage.postValue("File conversion failed.");
        }
    }


    /**
     * Validates the input passed through the entry fields for the upload request.
     * @param title the book's title
     * @param author the book's author
     * @param description the book's description
     * @param coverUri URI to the uploaded cover image
     * @param pdfUri URI to the uploaded PDF file
     * @return true if valid; otherwise false
     */
    private boolean validateFields(String title, String author, String description, Uri coverUri, Uri pdfUri) {
        if(title.isEmpty() || author.isEmpty() || description.isEmpty()) {
            errorMessage.postValue("All fields are required.");
            return false;
        }

        if(coverUri == null) {
            errorMessage.postValue("Please select a cover image.");
            return false;
        }

        if(pdfUri == null) {
            errorMessage.postValue("Please select a PDF file.");
            return false;
        }

        return true;
    }

    /**
     * Converts a Uri from Android's file picker into a Retrofit-ready MultipartBody.Part
     * This method:
     * 1. Opens the file as an InputStream.
     * 2. Copies it into a temporary file in the app's cache directory.
     * 3. Wraps it in a RequestBody and returns a MultipartBody.Part for the upload.
     *
     * @param context The context for accessing content resolver and cache dir
     * @param uri The file Uri selected by the user
     * @param partName The field name of the form-data part (for example: "coverImage")
     * @return MultipartBody.Part representing the uploaded file
     * @throws IOException if reading or writing the file fails
     */
    private MultipartBody.Part createMultipartFromUri(Context context, Uri uri, String partName) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if(inputStream == null) {
            throw new IOException("Unable to open input stream for URI: " + uri.toString());
        }

        // Create temporary file in cache directory with correct file extension
        File tempFile = File.createTempFile("upload", getFileExtension(context, uri), context.getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        /* Copy file content in chunks to avoid memory issues:
         * This block copies binary data from an InputStream (the file the user picked)
         * into an OutputStream (a temporary file).
         * It does this efficiently by reading the file in chunks (or buffers), not byte by byte.
         */
        byte[] buffer = new byte[4096]; // A temporary container (buffer) to hold bytes we read from the input file.
        // 4096 is a standard size that balances memory usage and I/O performance. Could also use 8192 or 1024.
        int bytesRead; // Store how many bytes were actually read in each pass
        while((bytesRead = inputStream.read(buffer)) != -1) {
            /*
             * inputStream.read(buffer) reads up to buffer.length bytes from the file into the buffer,
             * returns the number of bytes read or -1 if there is nothing left to read (end of file).
             * So, this loop continues as long as there's more data in the input file.
             * Each time it: fills the buffer -> stores how many bytes were read in bytesRead -> writes that data to the output file.
             */
            outputStream.write(buffer, 0, bytesRead); // Writes the valid path of the buffer to the output file. We write from index 0 to bytesRead.
        }

        // Closing streams
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        // Create RequestBody and wrap it as a MultipartBody.Part for Retrofit
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) throw new AssertionError();
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), tempFile);

        return MultipartBody.Part.createFormData(partName, tempFile.getName(), requestFile);
    }

    /**
     * Extracts the file extension for a given Uri.
     */
    private String getFileExtension(Context context, Uri uri) {
        String type = context.getContentResolver().getType(uri);
        return "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(type);
    }

    /**
     * Exposes upload success state to the UI.
     */
    public LiveData<Boolean> getUploadSuccess() {
        return uploadSuccess;
    }

    /**
     * Exposes error messages to the UI.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Clears current LiveData state. Useful when the upload screen is exited.
     */
    public void clear() {
        uploadSuccess.setValue(null);
        errorMessage.setValue(null);
    }
}