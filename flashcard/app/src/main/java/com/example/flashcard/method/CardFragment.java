package com.example.flashcard.method;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;

public class CardFragment extends Fragment {

    public static final String EXTRA_CARDSETID = "cardsetId";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_card, container, false);
        setupListView(layout);
        return layout;
    }

    private void setupListView(View view) {
        SQLiteOpenHelper flashcardDatabaseHelper = new FlashCardSQLiteHelper(getContext());
//        try {
//            SQLiteDatabase db = flashcardDatabaseHelper.getReadableDatabase();
//            Cursor cursor = db.query("SET",
//                    new String["NAME", ""])
//        }
    }
}