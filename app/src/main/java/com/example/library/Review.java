package com.example.library;


public class Review {
    private int bookId;
    private int userId;
    private float rating;
    private String review;
    private String username;

    public Review(int bookId, int userId, float rating, String review, String username) {
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.review = review;
        this.username = username;
    }

    // Геттеры и сеттеры

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

