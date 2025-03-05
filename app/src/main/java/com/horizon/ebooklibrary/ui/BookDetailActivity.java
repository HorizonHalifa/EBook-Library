package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.horizon.ebooklibrary.R;

// This activity will show the full book details when a user clicks on a book.
public class BookDetailActivity extends AppCompatActivity {
    private ImageView imageViewCover;
    private TextView textViewTitle, textViewAuthor, textViewDescription;
    private Button buttonReadBook, buttonMarkAsRead, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        imageViewCover = findViewById(R.id.imageViewCover);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonReadBook = findViewById(R.id.buttonReadBook);
        buttonMarkAsRead = findViewById(R.id.buttonMarkAsRead);
        buttonBack = findViewById(R.id.buttonBack);

        // Get book details from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        int coverImage = intent.getIntExtra("coverImage", R.drawable.ic_book_placeholder);

        // Set data to views
        textViewTitle.setText(title);
        textViewAuthor.setText(author);
        textViewDescription.setText(description);
        imageViewCover.setImageResource(coverImage);

        // Read Book Button
        buttonReadBook.setOnClickListener(v -> {
            Intent intent_read = new Intent(BookDetailActivity.this, PdfViewActivity.class);
            intent_read.putExtra("pdfFile", "book.pdf"); // TODO: Change this to actual file later.
            startActivity(intent_read);
        });

        // Mark As Read Button
        buttonMarkAsRead.setOnClickListener(v -> {
            // TODO: Implement Mark As Read Feature
        });

        // Back Button
        buttonBack.setOnClickListener(v -> { finish(); });
    }
}