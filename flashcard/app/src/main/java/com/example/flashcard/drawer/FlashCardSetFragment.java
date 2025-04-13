package com.example.flashcard.drawer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class FlashCardSetFragment extends Fragment implements View.OnClickListener{
    private SQLiteDatabase db;
    private boolean firstVisit;
    private Cursor setsCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstVisit = true;
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_flash_card_set, container, false);
        FloatingActionButton createSet = layout.findViewById(R.id.createSet);
        createSet.setOnClickListener(this);
        setupListView(layout);
        return layout;
    }

    private void onClickDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        builder.setTitle(R.string.create_set)
                .setView(editText)
                .setPositiveButton(R.string.ok,(d,i)->{
                    String setName = editText.getText().toString();
                    try {
                        FlashCardSQLiteHelper.insertCardSet(db, setName);
                        Fragment fragment = new FlashCardSetFragment();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.content_frame, fragment);
                        ft.commit();
                    } catch (SQLException e) {
                        Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                    }
                })
                .setNegativeButton(R.string.cancel_set, (d,i)->{})
                .show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createSet:
                onClickDone();
                break;
        }
    }

    private void setupListView(View view) {
        //Populate the list_set ListView from a cursor
        ListView listView = view.findViewById(R.id.list_sets);
       try {
           FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
           db = flashCardSQLiteHelper.getReadableDatabase();
           setsCursor = db.query("CARDSET", new String[] {"_id", "NAME"},
                   null, null, null, null, null);
               CursorAdapter setsAdapter =
                       new SimpleCursorAdapter(getContext(),
                               R.layout.item_cardset,
                               setsCursor,
                               new String[] {"NAME"},
                               new int[] {R.id.cardSetName}, 0);
               listView.setAdapter(setsAdapter);

       } catch (SQLException e) {
           Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
       }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
                     SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();
                     Cursor cardsCursor = db.query("CARD", new String[] {"_id"},
                             "CARDSETID=?", new String[] {Integer.toString((int)id)}, null, null, null);)
                {
                    if (cardsCursor.moveToFirst()) {

                    }
                    else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_set, Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.add, t -> {
                            Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                        });
                        snackbar.show();
                    }

                } catch (SQLException e) {
                    Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                }

            }
        });

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Cursor newCursor = db.query("CARDSET", new String[] {"_id", "NAME"},
//                null, null, null, null, null);
//        ListView listView = (ListView) getView().findViewById(R.id.list_sets);
//        CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
//        adapter.changeCursor(newCursor);
//        setsCursor = newCursor;
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        setsCursor.close();
        db.close();
    }

//    private class UpdateSetTask extends AsyncTask<Integer, Void, Boolean> {
//
//        protected Boolean doInBackground(Integer... integers) {
//            return null;
//        }
//    }
}