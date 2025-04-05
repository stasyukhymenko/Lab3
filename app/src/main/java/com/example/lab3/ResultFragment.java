package com.example.lab3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ResultFragment extends Fragment {
    private static final String ARG_BOOK = "selectedBook";
    private static final String ARG_YEAR = "selectedYear";

    private String selectedBook;
    private String selectedYear;

    public ResultFragment() {}

    public static ResultFragment newInstance(String book, String year) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOK, book);
        args.putString(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedBook = getArguments().getString(ARG_BOOK);
            selectedYear = getArguments().getString(ARG_YEAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        TextView txtResult = view.findViewById(R.id.txtResult);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        String result = getString(R.string.selected_option_prefix) + selectedBook +
                getString(R.string.year_prefix) + selectedYear;
        txtResult.setText(result);

        btnCancel.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        });

        return view;
    }
}