package com.horizon.ebooklibrary.adapters;

import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.model.Book;

import java.util.List;

/* This class extends RecyclerView.Adapter, meaning it controls how items appear in the RecyclerView.
 * It uses a custom ViewHolder to store the views for each book item.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    /*
     * How it works?
     * RecyclerView asks the Adapter how many items there are ( getItemCount ).
     * RecyclerView calls onCreateViewHolder to create views for the items.
     * RecyclerView calls onBindViewHolder to bind data to those views.
     * The user scrolls -> The RecyclerView reuses views to improve performance.
     */


    private Context context;
    private List<Book> bookList;

    /*
     * The costume ViewHolder class:
     * Hold references to UI elements (textTitle and textAuthor).
     * findViewById links these to book_item.xml.
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, descriptionTextView;
        ImageView coverImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            authorTextView = itemView.findViewById(R.id.textViewAuthor);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            coverImageView = itemView.findViewById(R.id.imageViewCover);
        }
    }

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    /*
     * inflate(book_item.xml) -> Creates a view for a single book item.
     * Wrap it in a BookViewHolder -> So it can hold references to it's TextViews.
     */
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    /*
     * This method is called when a new book needs to be displayed.
     * Steps:
     * 1. Get the book at position.
     * 2. Set it's title & author in TextViews.
     */
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.descriptionTextView.setText(book.getDescription());
        holder.coverImageView.setImageResource(book.getCoverImage()); // Set image from drawable
    }

    @Override
    // Returns the total number of books, so the RecyclerView knows how  many to display.
    public int getItemCount() {
        return bookList.size();
    }

}
