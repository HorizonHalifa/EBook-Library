package com.horizon.ebooklibrary.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.horizon.ebooklibrary.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Activity that displays a PDF file inside the app using AndroidPdfViewer (github repository)
 * <p>
 * This screen is opened from BookDetailActivity and displays the selected book's PDF.
 * The PDF is streamed from a remote backend URL passed via Intent.
 *
 */
public class PdfViewActivity extends AppCompatActivity {
    private PDFView pdfView;
    private final String TAG = "PdfViewActivity"; // For logcat testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdf_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        pdfView = findViewById(R.id.pdfView);
        Button buttonBack = findViewById(R.id.buttonBack);

        // Extract the PDF URL passed from BookDetailActivity
        Intent intent = getIntent();
        String pdfUrl = intent.getStringExtra("pdfFile");

        // Check if valid URL was passed
        if(pdfUrl == null || pdfUrl.isEmpty()) {
            Toast.makeText(this, "Error: No PDF file received!", Toast.LENGTH_LONG).show();
            Log.e("TAG", "Error: no PDF file received!");
            return;
        }
        Log.d(TAG, "Loading PDF from URL: " + pdfUrl);

        // Start loading the PDF
        displayPdfFromUrl(pdfUrl);

        // Handle Back button
        buttonBack.setOnClickListener(v -> {
            finish(); // Close this activity and return to BookDetailActivity
        });

    }

    /**
     * Downloads and displays the PDF file from the provided URL using a background thread
     * @param pdfUrl the full URL to the PDF file on the backend
     */
    private void displayPdfFromUrl (String pdfUrl){

        new Thread(() -> {
            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                runOnUiThread(() -> {
                    pdfView.fromStream(inputStream)
                            .enableSwipe(true) // Allow swiping to move between pages
                            .swipeHorizontal(false) // Vertical scrolling
                            .enableDoubletap(true) // Double-tap to zoom
                            .defaultPage(0) // Open on first page
                            .enableAntialiasing(true) // Better rendering of text
                            .scrollHandle(new DefaultScrollHandle(PdfViewActivity.this)) // Add scroll handle
                            .load();
                });


            } catch(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(PdfViewActivity.this, "Failed to load PDF.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "PDF Loading failed: " + e.getMessage());
                } );
            }
        } ).start();



        /* Older implementation for loading a pdf file locally.. maybe you want it for some test in the future so might as well keep it
        pdfView.fromAsset(pdfUrl)
                .enableSwipe(true) // Allow swiping to move between pages
                .swipeHorizontal(false) // Vertical scrolling
                .enableDoubletap(true) // Double-tap to zoom
                .defaultPage(0) // Open on first page
                .enableAntialiasing(true) // Better rendering of text
                .scrollHandle(new DefaultScrollHandle(this)) // Add scroll handle
                .load(); */
    }
}

