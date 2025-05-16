package com.horizon.ebooklibrary.model;

public class Book {
    private long id;
    private String title;
    private String author;
    private  String description;
    private String coverUrl; // URL to the book cover
    private String pdfUrl; // URL to the PDF file

    public Book() { }

    public Book(long  id, String title, String author, String description, String coverImage, String pdfUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverUrl = coverImage;
        this.pdfUrl = pdfUrl;
    }

    // Getters:
    public long getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }
}
