package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPage extends Fragment {

    private Button logoutButton;
    private Button addBookButton;
    private ApiService apiService;
    private int userId;
    private TextView textView;

    public AccountPage() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_page, container, false);

        logoutButton = view.findViewById(R.id.logout_button);
        addBookButton = view.findViewById(R.id.add_book_button);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "user");

        if ("admin".equals(userRole)) {
            addBookButton.setVisibility(View.VISIBLE);
        }

        logoutButton.setOnClickListener(v -> logoutUser());
        addBookButton.setOnClickListener(v -> openAddBookFragment());




        return view;
    }




    private void logoutUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userId");
        editor.remove("userRole");
        editor.apply();


        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
    }

    private void openAddBookFragment() {
        AddBookFragment addBookFragment = new AddBookFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, addBookFragment)
                .addToBackStack(null)
                .commit();
    }
}
