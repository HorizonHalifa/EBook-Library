package com.horizon.ebooklibrary.ui;

import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.viewmodel.UploadBookViewModel;

/**
 * Activity that allows an admin user to upload a new book to the library.
 * This includes selecting a cover image and PDF file, and entering metadata.
 * Sending the data to the backend using multipart/form-data.
 * This activity is accessible only to users with the 'ADMIN' role.
 */
public class UploadBookActivity extends AppCompatActivity {

    // UI references
    private EditText editTextTitle, editTextAuthor, editTextDescription;
    private TextView textViewCoverSelected, textViewPdfSelected;
    private Button buttonSelectCover, buttonSelectPdf, buttonUpload, buttonBack;

    // File URIs selected by the user
    private Uri selectedCoverUri = null;
    private Uri selectedPdfUri = null;

    // ViewModel handles logic and backend call
    private UploadBookViewModel uploadViewModel;

    /**
     * ActivityResultLauncher for selecting a cover image file (JPG or PNG).
     * This uses Android's Activity Result API.
     */
    private final ActivityResultLauncher<String> pickImageLaunches =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if(uri != null && isValidImage(uri)) {
                    selectedCoverUri = uri;
                    textViewCoverSelected.setText(getFileName(uri));
                } else {
                    showToast("Please select a JFP or PNG image.");
                }
            });

    private final ActivityResultLauncher<String> pickPdfLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if(uri != null && isValidPdf(uri)) {
                    selectedPdfUri = uri;
                    textViewPdfSelected.setText(getFileName(uri));
                } else {
                    showToast("Please select a valid PDF file.");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(); // Initialize all views from XML
        setupViewModel(); // Set up the ViewModel
        setupListeners(); // Attach onClick listeners
        observeViewModel(); // Bind LiveData to react to backend result

    }

    /**
     * Connects the ViewModel using ViewModelProvider.
     */
    private void setupViewModel() {
        uploadViewModel = new ViewModelProvider(this).get(UploadBookViewModel.class);
    }

    /**
     * Finds and binds UI components by their ID from the layout.
     */
    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewCoverSelected = findViewById(R.id.textViewCoverSelected);
        textViewPdfSelected = findViewById(R.id.textViewPdfSelected);
        buttonSelectCover = findViewById(R.id.buttonSelectCover);
        buttonSelectPdf = findViewById(R.id.buttonSelectPdf);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonBack = findViewById(R.id.buttonBack);

    }

    /**
     * Sets up the button click listeners for file selection, upload and back.
     */
    private void setupListeners() {

        buttonBack.setOnClickListener(v -> {
            finish(); // Close this activity and return to MainActivity
        });

        // Pick an image file (JPG/PNG)
        buttonSelectCover.setOnClickListener(v -> pickImageLaunches.launch("image/*"));

        buttonSelectPdf.setOnClickListener(v -> pickPdfLauncher.launch("application/pdf"));

        buttonUpload.setOnClickListener(v -> {
                    String title = editTextTitle.getText().toString().trim();
                    String author = editTextAuthor.getText().toString().trim();
                    String description = editTextDescription.getText().toString().trim();

                    // Pass everything to the ViewModel for validation and upload
                    uploadViewModel.uploadBook(this, title, author, description, selectedCoverUri, selectedPdfUri);
                });
    }

    /**
     * Observes LiveData from the ViewModel and reacts to upload success or error.
     */
    private void observeViewModel() {
        uploadViewModel.getUploadSuccess().observe(this, success -> {
            if(success!= null && success) {
                showToast("Book uploaded successfully!");
                uploadViewModel.clear(); // Clear the state before exit
                finish(); // Close the screen and return to previous activity
            }
        });

        uploadViewModel.getErrorMessage().observe(this, error -> {
            if(error != null) {
                showToast(error);
            }
        });
    }

    /**
     * Checks if the selected image file is a JPG or PNG.
     */
    private boolean isValidImage(Uri uri) {
        String type = getContentResolver().getType(uri);
        return type != null && (
                type.equalsIgnoreCase("image/jpeg") ||
                        type.equalsIgnoreCase("image/jpg") ||
                        type.equalsIgnoreCase("image/png") ||
                        type.equalsIgnoreCase("image/x-png")
        );

    }

    /**
     * Checks if selected file is a PDF.
     */
    private boolean isValidPdf(Uri uri) {
        String type = getContentResolver().getType(uri);
        return type != null && type.equals("application/pdf");
    }

    /**
     * Extracts the display name of the file selected by the user.
     * This name is shown to the user in a TextView.
     * @param uri The URI of the selected file
     * @return A user-friendly file name string
     */
    private String getFileName(Uri uri) {
        String result = "selected file";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) result = cursor.getString(nameIndex);
            }
        }
        return result;
    }

    /**
     * Displays a short toast message.
     */
    private void showToast(String message) {
        Toast.makeText(UploadBookActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
