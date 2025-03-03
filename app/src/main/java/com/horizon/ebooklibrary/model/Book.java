package com.horizon.ebooklibrary.model;

public class Book {
    private final String title;
    private final String author;
    private final String description;
    private final int coverImage; // Resource ID for local images.

    public Book(String title, String author, String description, int coverImage) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverImage = coverImage;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getCoverImage() {
        return coverImage;
    }
}
