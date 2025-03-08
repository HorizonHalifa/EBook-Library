package com.horizon.ebooklibrary.ui;

import android.os.Bundle;

import android.view.View;
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

/*
 * This class represents the main activity of the application which is the book list.
 * Separate books into two lists: unreadBooks and readBooks.
 * Display the books in two RecyclerViews accordingly.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUnreadBooks, recyclerViewReadBooks;
    private BookAdapter unreadBooksAdapter, readBooksAdapter;
    private List<Book> unreadBooks, readBooks;

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
        recyclerViewUnreadBooks = findViewById(R.id.recyclerViewUnreadBooks);
        recyclerViewReadBooks = findViewById(R.id.recyclerViewReadBooks);

        recyclerViewUnreadBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReadBooks.setLayoutManager(new LinearLayoutManager(this));

        // Initialize book lists
        unreadBooks = new ArrayList<>();
        readBooks = new ArrayList<>();

        // Load nooks, TODO: for now using dummy data
        loadBooks();

        // Initialize adapters
        unreadBooksAdapter = new BookAdapter(this, unreadBooks);
        readBooksAdapter = new BookAdapter(this, readBooks);

        recyclerViewUnreadBooks.setAdapter(unreadBooksAdapter);
        recyclerViewReadBooks.setAdapter(readBooksAdapter);

        // Force update RecyclerViews
        unreadBooksAdapter.notifyDataSetChanged();
        readBooksAdapter.notifyDataSetChanged();

        // Ensure Read Books section is always visible
        findViewById(R.id.textViewReadTitle).setVisibility(View.VISIBLE);
        recyclerViewReadBooks.setVisibility(View.VISIBLE);
    }

    private void loadBooks() {
        unreadBooks.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "A novel set in the Roaring Twenties.", R.drawable.ic_book_placeholder));
        unreadBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", "A powerful novel about racism and justice.", R.drawable.ic_book_placeholder));
        unreadBooks.add(new Book("1984", "George Orwell", "A dystopian novel about totalitarianism.", R.drawable.ic_book_placeholder));
        unreadBooks.add(new Book("Moby-Dick", "Herman Melville", "A tale of obsession and revenge at sea.", R.drawable.ic_book_placeholder));
        unreadBooks.add(new Book("Pride and Prejudice", "Jane Austen", "A classic romantic novel.", R.drawable.ic_book_placeholder));

        readBooks.add(new Book("סיפורה של מספרת הסיפורים", "חנה קריצמן", "זהו תיאורו של הסיפור המרתק", R.drawable.the_story_tellers_story));
        readBooks.add(new Book("התמודדות", "אורי כרמי", "זהו תיאורו של הסיפור המרתק", R.drawable.coping));
        readBooks.add(new Book("ממך למדתי ללכת", "ורד אזולאי", "זהו תיאורו של הסיפור המרתק", R.drawable.from_you_i_learned_to_walk));

        // Debugging logs
        System.out.println("✅ Unread Books Count: " + unreadBooks.size());
        System.out.println("✅ Read Books Count: " + readBooks.size());
    }
}