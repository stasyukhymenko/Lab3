package com.example.lab3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookSelectionAdapter extends RecyclerView.Adapter<BookSelectionAdapter.BookSelectionViewHolder> {

    private List<BookSelection> selections = new ArrayList<>();
    private final OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(BookSelection selection);
    }

    public BookSelectionAdapter(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public BookSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book_selection, parent, false);
        return new BookSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookSelectionViewHolder holder, int position) {
        BookSelection currentSelection = selections.get(position);
        holder.textViewBookName.setText(currentSelection.bookName);
        holder.textViewSelectionYear.setText(currentSelection.selectionYear);

        holder.buttonDeleteItem.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(currentSelection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selections.size();
    }

    public void submitList(List<BookSelection> newSelections) {
        this.selections.clear();
        this.selections.addAll(newSelections);
        notifyDataSetChanged();
    }

    static class BookSelectionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBookName;
        TextView textViewSelectionYear;
        ImageButton buttonDeleteItem;

        public BookSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName = itemView.findViewById(R.id.textViewBookName);
            textViewSelectionYear = itemView.findViewById(R.id.textViewSelectionYear);
            buttonDeleteItem = itemView.findViewById(R.id.buttonDeleteItem);
        }
    }
}