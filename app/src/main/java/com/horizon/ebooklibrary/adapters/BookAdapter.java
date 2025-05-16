package com.horizon.ebooklibrary.adapters;

import android.content.Intent;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.horizon.ebooklibrary.R;
import com.horizon.ebooklibrary.model.Book;
import com.horizon.ebooklibrary.ui.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter to display a list of books.
 * Each book includes title, author, description and cover image,
 * Tapping a book opens the BookDetailActivity
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    /*
     * How it works?
     * RecyclerView asks the Adapter how many items there are ( getItemCount ).
     * RecyclerView calls onCreateViewHolder to create views for the items.
     * RecyclerView calls onBindViewHolder to bind data to those views.
     * The user scrolls -> The RecyclerView reuses views to improve performance.
     */


    private final Context context;
    private List<Book> bookList;

    /*
     * The costume ViewHolder class:
     * Hold references to UI elements (textTitle and textAuthor).
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
        this.bookList = bookList != null ? bookList : new ArrayList<>();
    }

    public void setBooks(List<Book> newBooks) {
        this.bookList = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * inflate(book_item.xml) -> Creates a view for a single book item.
     * Wrap it in a BookViewHolder -> So it can hold references to it's TextViews.
     */
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }


    /**
     * This method is called when a new book needs to be displayed.
     * Steps:
     * 1. Get the book at position.
     * 2. Set it's title & author in TextViews.
     */
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.descriptionTextView.setText(book.getDescription());

        // Load image from coverUrl using Glide
        Glide.with(context)
                        .load(book.getCoverUrl())
                                .placeholder(R.drawable.ic_book_placeholder)
                                        .error(R.drawable.ic_image_error)
                                                .into(holder.coverImageView);

        // Open book details screen on click
        holder.itemView.setOnClickListener(v -> {
            /*
             * When a book is clicked, it starts BookDetailActivity.
             * Passes title, author, description and cover image to the next screen.
             */
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("bookId", book.getId());
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("description", book.getDescription());
            intent.putExtra("coverUrl", book.getCoverUrl());
            intent.putExtra("pdfUrl", book.getPdfUrl());
            context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of books, so the RecyclerView knows how  many to display.
     */
    @Override
    public int getItemCount() {
        return bookList.size();
    }

}
