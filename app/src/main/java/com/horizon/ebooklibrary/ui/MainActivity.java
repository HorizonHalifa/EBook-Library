package com.horizon.ebooklibrary.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.adapters.BookAdapter;
import com.horizon.ebooklibrary.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

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

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // TODO: Consider changing to a GridLayoutManager.


        // Initialize book list and add sample books
        loadBooks();

        // Initialize adapter and attach it
        bookAdapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(bookAdapter);
    }

    private void loadBooks() {
        bookList = new ArrayList<>();
        bookList.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "A novel set in the Roaring Twenties.", R.drawable.ic_book_placeholder));
        bookList.add(new Book("To Kill a Mockingbird", "Harper Lee", "A powerful novel about racism and justice.", R.drawable.ic_book_placeholder));
        bookList.add(new Book("1984", "George Orwell", "A dystopian novel about totalitarianism.", R.drawable.ic_book_placeholder));
        bookList.add(new Book("Moby-Dick", "Herman Melville", "A tale of obsession and revenge at sea.", R.drawable.ic_book_placeholder));
        bookList.add(new Book("Pride and Prejudice", "Jane Austen", "A classic romantic novel.", R.drawable.ic_book_placeholder));
    }
}