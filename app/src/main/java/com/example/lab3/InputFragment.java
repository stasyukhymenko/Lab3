package com.example.lab3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputFragment extends Fragment {

    private Spinner spinnerBooks;
    private RadioGroup radioGroupYears;
    private Button btnOk;
    private Button btnCancelMain;
    private Button btnOpen;

    private final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private AppDatabase db;

    public InputFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        db = AppDatabase.getDatabase(getActivity().getApplicationContext());

        spinnerBooks = view.findViewById(R.id.spinnerBooks);
        radioGroupYears = view.findViewById(R.id.radioGroupYears);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancelMain = view.findViewById(R.id.btnCancelMain);
        btnOpen = view.findViewById(R.id.btnOpen);

        String defaultBookOption = getString(R.string.default_book_option);
        String[] books = {
                defaultBookOption, "Дж.К. Роулінг - Гаррі Поттер", "Джордж Орвелл - 1984",
                "Всеволод Нестайко - Тореадори з Васюківки", "Герберт Шілдт - Java. Повне керівництво"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, books);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBooks.setAdapter(adapter);

        btnOk.setOnClickListener(v -> processOkClick(view));
        btnCancelMain.setOnClickListener(v -> {
            spinnerBooks.setSelection(0);
            radioGroupYears.clearCheck();
        });
        btnOpen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DisplayActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void processOkClick(View view) {
        String selectedBook = spinnerBooks.getSelectedItem().toString();
        int selectedRadioId = radioGroupYears.getCheckedRadioButtonId();
        String defaultBookOption = getString(R.string.default_book_option);

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder(getString(R.string.validation_incomplete_message));
        if (selectedBook.equals(defaultBookOption)) { isValid = false; errorMessage.append(getString(R.string.validation_select_book)); }
        if (selectedRadioId == -1) { isValid = false; errorMessage.append(getString(R.string.validation_select_year)); }


        if (isValid) {
            RadioButton selectedRadioButton = view.findViewById(selectedRadioId);
            String selectedYear = selectedRadioButton.getText().toString();

            saveDataToDb(selectedBook, selectedYear);

            ResultFragment resultFragment = ResultFragment.newInstance(selectedBook, selectedYear);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, resultFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.validation_incomplete_title)
                    .setMessage(errorMessage.toString().trim())
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    private void saveDataToDb(String book, String year) {
        BookSelection newSelection = new BookSelection(book, year);

        databaseWriteExecutor.execute(() -> {
            db.bookSelectionDao().insert(newSelection);
            uiHandler.post(() -> {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), R.string.save_success, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}