package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.util.TokenManager;
import com.horizon.ebooklibrary.viewmodel.BookDetailViewModel;

/**
 * Displays detailed information about a selected book.
 * Users can:
 * Read the book (open the PDF).
 * Mark the book as read / unread.
 * Return to the previous screen.
 */
public class BookDetailActivity extends AppCompatActivity {

    // UI components
    private ImageView imageViewCover;
    private TextView textViewTitle, textViewAuthor, textViewDescription;
    private Button buttonReadBook, buttonMarkAsRead, buttonMarkAsUnread, buttonDeleteBook, buttonBack;
    private BookDetailViewModel viewModel;


    private long bookId;
    private String pdfUrl;

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


        initViews();
        setupViewModel();
        setupListeners();
        loadBookDataFromIntent();

    }

    /**
     * Binds all view references from the layout
     */
    private void initViews() {
        imageViewCover = findViewById(R.id.imageViewCover);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonReadBook = findViewById(R.id.buttonReadBook);
        buttonMarkAsRead = findViewById(R.id.buttonMarkAsRead);
        buttonMarkAsUnread = findViewById(R.id.buttonMarkAsUnread);
        buttonDeleteBook = findViewById(R.id.buttonDeleteBook);
        buttonBack = findViewById(R.id.buttonBack);
    }

    /**
     * Sets up the view model for the class and connects the needed listeners and observers
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);
        viewModel.getOperationMessage().observe(this, message ->
                Toast.makeText(BookDetailActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    /**
     * Sets up button listeners
     */
    private void setupListeners() {
        buttonReadBook.setOnClickListener(v -> {
            Intent intent = new Intent(this, PdfViewActivity.class);
            intent.putExtra("pdfFile", pdfUrl);
            startActivity(intent);
        });

        if("ADMIN".equals(TokenManager.getInstance().getUserRole())) {
            buttonDeleteBook.setVisibility(View.VISIBLE);
            buttonDeleteBook.setOnClickListener(v -> viewModel.deleteBook(bookId));
        } else {
            buttonDeleteBook.setVisibility(View.GONE);
        }

        buttonMarkAsRead.setOnClickListener(v -> viewModel.markAsRead(bookId));
        buttonMarkAsUnread.setOnClickListener(v -> viewModel.markAsUnread(bookId));
        buttonBack.setOnClickListener(v -> finish());
    }

    /**
     * Loads book data
     */
    private void loadBookDataFromIntent() {
        Intent intent = getIntent();
        bookId = intent.getLongExtra("bookId", -1);
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String coverUrl = intent.getStringExtra("coverUrl");
        pdfUrl = intent.getStringExtra("pdfUrl");

        textViewTitle.setText(title);
        textViewAuthor.setText(author);
        textViewDescription.setText(description);

        Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.ic_book_placeholder)
                .error(R.drawable.ic_image_error)
                .into(imageViewCover);
    }
}