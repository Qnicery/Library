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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookFragment extends Fragment {

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText genreEditText;
    private EditText descriptionEditText;
    private EditText ratingEditText;
    private Button addButton;
    private ApiService apiService;
    private int userId;
    private BookUpdateListener bookUpdateListener;

    public AddBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookUpdateListener) {
            bookUpdateListener = (BookUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BookUpdateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookUpdateListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        titleEditText = view.findViewById(R.id.title);
        authorEditText = view.findViewById(R.id.author);
        genreEditText = view.findViewById(R.id.genre);
        descriptionEditText = view.findViewById(R.id.description);
        addButton = view.findViewById(R.id.add_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.120/library/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        String userRole = sharedPreferences.getString("userRole", "user");

        if (!"admin".equals(userRole)) {
            Toast.makeText(getContext(), "Access denied", Toast.LENGTH_SHORT).show();
            // Вернуться назад или закрыть этот фрагмент
            getActivity().getSupportFragmentManager().popBackStack();
        }

        addButton.setOnClickListener(v -> addBook());

        return view;
    }

    private void addBook() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String genre = genreEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        float rating = 0;
        List<String> reviews = new ArrayList<>(); // Можно добавить поле для ввода отзывов

        Book book = new Book(0, title, author, genre, description, rating, reviews);
        apiService.addBook(book, userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("AddBook", "Response: " + responseBody);
                        Toast.makeText(getContext(), "Book added", Toast.LENGTH_SHORT).show();
                        // Вызов обновления списка книг
                        if (bookUpdateListener != null) {
                            bookUpdateListener.onBookListUpdated();
                        }
                        //getFragmentManager().popBackStack(); // Вернуться назад после добавления книги
                    } else {
                        String errorBody = response.errorBody().string();
                        Log.e("AddBook", "Failed to add book: " + errorBody);
                        Toast.makeText(getContext(), "Failed to add book", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Books", t.getMessage());
                Toast.makeText(getContext(), "Failed to add book: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



