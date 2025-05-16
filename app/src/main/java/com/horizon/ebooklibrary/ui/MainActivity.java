package com.horizon.ebooklibrary.ui;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.adapters.BookAdapter;
import com.horizon.ebooklibrary.model.Book;
import com.horizon.ebooklibrary.service.FirebaseTokenService;
import com.horizon.ebooklibrary.util.TokenManager;
import com.horizon.ebooklibrary.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

/*
 * This class represents the main activity of the application which is the book list.
 * Separate books into two lists: unreadBooks and readBooks.
 * Display the books in two RecyclerViews accordingly.
 * Admin users can also access the Upload Book activity.
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private RecyclerView recyclerViewUnreadBooks, recyclerViewReadBooks;
    private Button buttonUploadBook;

    // RecyclerView Adapters
    private BookAdapter unreadBooksAdapter, readBooksAdapter;

    private MainViewModel mainViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize token manager
        TokenManager.init(getApplicationContext());

        // Bind views and set up logic
        initViews();
        setupRecyclerView();
        setupUploadButtonAdmin();
        setupViewModel();


    }

    /**
     * refreshes the RecyclerViews when the activity is resumed to show updated state of books.
     * useful when leaving the activity to the BookDetail activity and marking a book as read unread
     * and then returning to this activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mainViewModel != null) {
            mainViewModel.loadBooks(); // Reload the latest state of books
        }
    }


    /**
     * Binds all view references from the layout
     */
    private void initViews() {
        recyclerViewUnreadBooks = findViewById(R.id.recyclerViewUnreadBooks);
        recyclerViewReadBooks = findViewById(R.id.recyclerViewReadBooks);
        buttonUploadBook = findViewById(R.id.buttonUploadBook);
    }

    /**
     * Set up the RecyclerViews with adapters and linear layout managers.
     */
    private void setupRecyclerView() {
        // Setup empty adapters
        unreadBooksAdapter = new BookAdapter(this, new ArrayList<>());
        readBooksAdapter = new BookAdapter(this, new ArrayList<>());

        // Setup Layout managers
        recyclerViewUnreadBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReadBooks.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewUnreadBooks.setAdapter(unreadBooksAdapter);
        recyclerViewReadBooks.setAdapter(readBooksAdapter);

    }


    /**
     * Shows the Upload Book button only if the logged in user is an ADMIN
     */
    private void setupUploadButtonAdmin() {
        Log.d("TOKEN_ROLE", "Role: " + TokenManager.getInstance().getUserRole());
        if("ADMIN".equals(TokenManager.getInstance().getUserRole())) {
            buttonUploadBook.setVisibility(View.VISIBLE);
            buttonUploadBook.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, UploadBookActivity.class);
                startActivity(intent);
            });
        } else {
            buttonUploadBook.setVisibility(View.GONE);
        }


    }

    private void setupViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // observe read books
        mainViewModel.getUnreadBooks().observe(this, books -> {
            unreadBooksAdapter.setBooks(books);
        });

        // observe unread books
        mainViewModel.getReadBooks().observe(this, books -> {
            readBooksAdapter.setBooks(books);
        });

        // observe errors
        mainViewModel.getErrorMessage().observe(this, error -> {
            if(error != null) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Start loading books from the backend
        mainViewModel.loadBooks();
    }
}