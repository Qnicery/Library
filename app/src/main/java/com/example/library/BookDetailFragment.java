package com.example.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class BookDetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private TextView descriptionTextView;
    private TextView ratingTextView;
    private RecyclerView reviewsRecyclerView;
    private Button addReviewButton;
    private ApiService apiService;
    private int bookId;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        titleTextView = view.findViewById(R.id.book_title);
        authorTextView = view.findViewById(R.id.book_author);
        genreTextView = view.findViewById(R.id.book_genre);
        descriptionTextView = view.findViewById(R.id.book_description);
        ratingTextView = view.findViewById(R.id.book_rating);
        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view);
        addReviewButton = view.findViewById(R.id.add_review_button);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.120/library/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        if (getArguments() != null) {
            bookId = getArguments().getInt("id");
            fetchBookDetails(bookId);
        }

        addReviewButton.setOnClickListener(v -> openAddReviewFragment());

        return view;
    }

    private void fetchBookDetails(int bookId) {
        apiService.getBook(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Book book = response.body();
                    titleTextView.setText(book.getTitle());
                    authorTextView.setText(book.getAuthor());
                    genreTextView.setText(book.getGenre());
                    descriptionTextView.setText(book.getDescription());
                    ratingTextView.setText("Rating: " + book.getRating());

                    fetchBookReviews(bookId);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }

    private void fetchBookReviews(int bookId) {
        apiService.getBookReviews(bookId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }

    private void openAddReviewFragment() {
        AddReviewFragment addReviewFragment = new AddReviewFragment();
        Bundle args = new Bundle();
        args.putInt("book_id", bookId);
        addReviewFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, addReviewFragment)
                .addToBackStack(null)
                .commit();
    }
}




