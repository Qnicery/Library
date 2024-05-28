package com.example.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddReviewFragment extends Fragment {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ApiService apiService;
    private int userId;
    private int bookId;

    public AddReviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        reviewEditText = view.findViewById(R.id.review);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submit_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.120/library/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (getArguments() != null) {
            bookId = getArguments().getInt("book_id");
        }

        Log.d("AddReviewFragment", "User ID: " + userId + ", Book ID: " + bookId);

        submitButton.setOnClickListener(v -> getUserAndSubmitReview());

        return view;
    }

    private void getUserAndSubmitReview() {
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String username = response.body().getUsername();
                    submitReview(username);
                } else {
                    Toast.makeText(getContext(), "Failed to get username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReview(String username) {
        String reviewText = reviewEditText.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (rating == 0) {
            Toast.makeText(getContext(), "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review(bookId, userId, rating, reviewText, username);
        Log.d("AddReviewFragment", "Submitting review: bookId=" + bookId + ", userId=" + userId + ", username=" + username + ", rating=" + rating);

        apiService.addReview(review).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Review added", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("AddReview", "Failed to add review: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Failed to add review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AddReview", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to add review: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}




