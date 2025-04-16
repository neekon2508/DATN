package com.example.flashcard.method;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.Card;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.drawer.FlashCardSetFragment;
import com.google.android.material.snackbar.Snackbar;

public class ListCardFragment extends Fragment {

    private static int cardsetId=0;
    private static Card[] cards;
    private SQLiteDatabase db;
    private Cursor cardsCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            cardsetId = getArguments().getInt("cardsetId", 0);
        }

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_list_card, container, false);
        setupListView(layout);
        return layout;
    }

//    private void setupCardArray(View view) {
//
//        try {
//            FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
//            db = flashCardSQLiteHelper.getReadableDatabase();
//            cardsCursor = db.query("CARD", new String[] {"_id", "FRONTTEXT","BACKTEXT"},
//                    "CARDSETID=?", new String[] {Integer.toString((int)cardsetId)}, null, null, null);
//            if( cardsCursor != null && cardsCursor.moveToFirst()) {
//                cards = new Card[cardsCursor.getCount()];
//                for(int i =0; i< cards.length; ++i) {
//                    cards[i] = new Card(cardsCursor.getInt(0),
//                            cardsCursor.getString(1),
//                            cardsCursor.getString(2));
//                    cardsCursor.moveToNext();
//                }
//            }
//        } catch (SQLException e) {
//            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
//        }
//    }

    private void setupListView(View view) {
        //Populate the list_set ListView from a cursor
        ListView listView = view.findViewById(R.id.list_cards);
        try {
            FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
            db = flashCardSQLiteHelper.getReadableDatabase();
            cardsCursor = db.query("CARD", new String[] {"_id", "FRONTTEXT", "BACKTEXT"},
                    "CARDSETID=?", new String[] {String.valueOf((int)cardsetId)}, null, null, null);
            CursorAdapter setsAdapter =
                    new SimpleCursorAdapter(getContext(),
                            R.layout.item_card,
                            cardsCursor,
                            new String[] {"FRONTTEXT","BACKTEXT"},
                            new int[] {R.id.item_card_front_text, R.id.item_card_back_text}, 0);
            listView.setAdapter(setsAdapter);

        } catch (SQLException e) {
            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
                return true;
            }
        });
    }
}