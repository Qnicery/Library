package com.example.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;
    private ApiService apiService;
    private EditText searchInput;
    private Button searchButton;
    private Button sortRatingButton;
    private Button sortAlphaButton;

    public HomePage() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        sortRatingButton = view.findViewById(R.id.sort_rating_button);
        sortAlphaButton = view.findViewById(R.id.sort_alpha_button);

        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, book -> {
            // Обработчик нажатия на элемент списка книг
            Bundle bundle = new Bundle();
            bundle.putInt("id", book.getId());

            BookDetailFragment detailFragment = new BookDetailFragment();
            detailFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }, book -> addBookToLibrary(book));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.120/library/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        fetchBooks();

        searchButton.setOnClickListener(v -> searchBooks());
        sortRatingButton.setOnClickListener(v -> sortBooksByRating());
        sortAlphaButton.setOnClickListener(v -> sortBooksAlphabetically());

        return view;
    }

    public void fetchBooks() {
        apiService.getBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList.clear();
                    bookList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }

    private void searchBooks() {
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) {
            fetchBooks();
            return;
        }

        apiService.searchBooks(query).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookList.clear();
                    bookList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }

    private void sortBooksByRating() {
        Collections.sort(bookList, (book1, book2) -> Float.compare(book2.getRating(), book1.getRating()));
        adapter.notifyDataSetChanged();
    }

    private void sortBooksAlphabetically() {
        Collections.sort(bookList, Comparator.comparing(Book::getTitle));
        adapter.notifyDataSetChanged();
    }

    private void addBookToLibrary(Book book) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        apiService.addBookToLibrary(new LibraryRequest(userId, book.getId())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String message = jsonResponse.getString("message");
                        String status = jsonResponse.getString("status");

                        if ("exists".equals(status)) {
                            Toast.makeText(getContext(), "Book already in library", Toast.LENGTH_SHORT).show();
                        } else if ("added".equals(status)) {
                            Toast.makeText(getContext(), "Book added to library", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Unknown response from server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to parse server response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to add book to library", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to add book to library", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




