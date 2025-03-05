package com.horizon.ebooklibrary.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.horizon.ebooklibrary.R;


public class PdfViewActivity extends AppCompatActivity {
    private PDFView pdfView;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfView = findViewById(R.id.pdfView);
        buttonBack = findViewById(R.id.buttonBack);

        // Get the PDF file from the intent
        Intent intent_read = getIntent();
        String pdfFileName = intent_read.getStringExtra("pdfFile");

        // Debugging logs
        if(pdfFileName == null) {
            Toast.makeText(this, "Error: No PDF file received!", Toast.LENGTH_LONG).show();
            Log.e("PdfViewActivity", "X no PDF file received!");
            return;
        } else {
            Log.d("PdfViewActivity", "V Received PDF file " + pdfFileName);
        }


        // Display the PDF
        displayFromAssets(pdfFileName);

        // Handle Back Button Click
        buttonBack.setOnClickListener(v -> {
            finish(); // Close this activity and return to BookDetailActivity
        });
    }

    private void displayFromAssets (String assetFileName){
        pdfView.fromAsset(assetFileName)
                .enableSwipe(true) // Allow swiping to move between pages
                .swipeHorizontal(false) // Vertical scrolling
                .enableDoubletap(true) // Double-tap to zoom
                .defaultPage(0) // Open on first page
                .enableAntialiasing(true) // Better rendering of text
                .scrollHandle(new DefaultScrollHandle(this)) // Add scroll handle
                .load();
    }
}

