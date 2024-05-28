package com.example.library;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api.php?action=register")
    Call<ResponseBody> register(@Body User user);

    @POST("api.php?action=login")
    Call<ResponseBody> login(@Body User user);

    @GET("api.php?action=getBooks")
    Call<List<Book>> getBooks();

    @GET("api.php?action=searchBooks")
    Call<List<Book>> searchBooks(@Query("query") String query);

    @GET("api.php?action=getBook")
    Call<Book> getBook(@Query("id") int id);

    @POST("api.php?action=addBook")
    Call<ResponseBody> addBook(@Body Book book, @Query("user_id") int userId);

    @POST("api.php?action=addReview")
    Call<ResponseBody> addReview(@Body Review review);

    @GET("api.php?action=getBookReviews")
    Call<List<Review>> getBookReviews(@Query("book_id") int bookId);

    @GET("api.php?action=getUser")
    Call<User> getUser(@Query("id") int userId);

    @POST("api.php?action=addBookToLibrary")
    Call<ResponseBody> addBookToLibrary(@Body LibraryRequest request);

    @GET("api.php?action=getUserLibrary")
    Call<List<Book>> getUserLibrary(@Query("user_id") int userId);

    @DELETE("api.php?action=removeBookFromLibrary")
    Call<ResponseBody> removeBookFromLibrary(@Query("userId") int userId, @Query("bookId") int bookId);

}