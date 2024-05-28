package com.example.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PersonalBookAdapter extends RecyclerView.Adapter<PersonalBookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private OnBookClickListener onBookClickListener;
    private OnRemoveFromLibraryClickListener onRemoveFromLibraryClickListener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public interface OnRemoveFromLibraryClickListener {
        void onRemoveFromLibraryClick(Book book);
    }

    public PersonalBookAdapter(List<Book> bookList, OnBookClickListener onBookClickListener, OnRemoveFromLibraryClickListener onRemoveFromLibraryClickListener) {
        this.bookList = bookList;
        this.onBookClickListener = onBookClickListener;
        this.onRemoveFromLibraryClickListener = onRemoveFromLibraryClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_book, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.genreTextView.setText(book.getGenre());
        holder.descriptionTextView.setText(book.getDescription());
        holder.ratingTextView.setText("Rating: " + book.getRating());
        holder.itemView.setOnClickListener(v -> onBookClickListener.onBookClick(book));
        holder.removeFromLibraryButton.setOnClickListener(v -> onRemoveFromLibraryClickListener.onRemoveFromLibraryClick(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView authorTextView;
        public TextView genreTextView;
        public TextView descriptionTextView;
        public TextView ratingTextView;
        public Button removeFromLibraryButton;

        public BookViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            genreTextView = itemView.findViewById(R.id.book_genre);
            descriptionTextView = itemView.findViewById(R.id.book_description);
            ratingTextView = itemView.findViewById(R.id.book_rating);
            removeFromLibraryButton = itemView.findViewById(R.id.remove_from_library_button);
        }
    }
}
