package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayActivity extends AppCompatActivity implements BookSelectionAdapter.OnDeleteClickListener {

    private RecyclerView recyclerViewSelections;
    private TextView textViewEmpty;
    private Button btnDeleteAll;
    private BookSelectionAdapter adapter;
    private AppDatabase db;
    private final ExecutorService databaseExecutor = Executors.newFixedThreadPool(2);
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        setTitle(R.string.saved_data_title);
        db = AppDatabase.getDatabase(getApplicationContext());

        textViewEmpty = findViewById(R.id.textViewEmpty);
        recyclerViewSelections = findViewById(R.id.recyclerViewSelections);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        recyclerViewSelections.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookSelectionAdapter(this);
        recyclerViewSelections.setAdapter(adapter);

        loadDataFromDb();

        btnDeleteAll.setOnClickListener(v -> deleteAllData());
    }

    private void loadDataFromDb() {
        databaseExecutor.execute(() -> {
            List<BookSelection> selections = db.bookSelectionDao().getAllSelections();
            uiHandler.post(() -> {
                if (selections.isEmpty()) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    recyclerViewSelections.setVisibility(View.GONE);
                    btnDeleteAll.setVisibility(View.GONE);
                } else {
                    textViewEmpty.setVisibility(View.GONE);
                    recyclerViewSelections.setVisibility(View.VISIBLE);
                    btnDeleteAll.setVisibility(View.VISIBLE);
                    adapter.submitList(selections);
                }
            });
        });
    }

    private void deleteAllData() {
        databaseExecutor.execute(() -> {
            db.bookSelectionDao().deleteAll();
            uiHandler.post(() -> {
                Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                loadDataFromDb();
            });
        });
    }

    @Override
    public void onDeleteClick(BookSelection selection) {
        databaseExecutor.execute(() -> {
            db.bookSelectionDao().delete(selection);
            uiHandler.post(() -> {
                Toast.makeText(this, "Запис видалено", Toast.LENGTH_SHORT).show();
                loadDataFromDb();
            });
        });
    }
}