package com.example.library;

import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String description;
    private float rating;
    private List<String> reviews;

    public Book(int id, String title, String author, String genre, String description, float rating, List<String> reviews) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.rating = rating;
        this.reviews = reviews;
    }

    public float getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public int getId() {
        return id;
    }
}