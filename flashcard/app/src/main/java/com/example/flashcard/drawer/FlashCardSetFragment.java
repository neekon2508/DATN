package com.example.flashcard.drawer;

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
import android.text.Spannable;
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
import com.example.flashcard.data.FlashCardSQLiteHelper;

import com.example.flashcard.method.CreateCardActivity;
import com.example.flashcard.method.LearnActivity;
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
                        Intent intent = new Intent(getActivity(), LearnActivity.class);
                        intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(id));
                        startActivity(intent);
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_set, Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.add, t -> {
                            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                            intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(id));
                            startActivity(intent);
                        });
                        snackbar.show();
                    }

                } catch (SQLException e) {
                    Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.getMenuInflater().inflate(R.menu.menu_card_set, popup.getMenu());
                popup.setGravity(Gravity.CENTER);
                popup.setOnMenuItemClickListener(item -> {
                    switch(item.getItemId()) {
                        case R.id.action_add_card:
                            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                            intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(id));
                            startActivity(intent);
                            return true;
                        case R.id.action_see_all_card:
                            Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.action_change_name_set:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            EditText editText = new EditText(getActivity());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            editText.setLayoutParams(lp);
                            editText.setText(((TextView)view).getText().toString());
                            builder.setTitle(R.string.menu_cardset_change_name)
                                    .setView(editText)
                                    .setPositiveButton(R.string.ok,(d,i)->{
                                        String changeSetName = editText.getText().toString();
                                        try {
                                            FlashCardSQLiteHelper.updateCardSet(db, (int)id, changeSetName);
                                            ((TextView)view).setText(changeSetName);
                                            Toast.makeText(getContext(), R.string.complete, Toast.LENGTH_SHORT).show();
                                        } catch (SQLException e) {
                                            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel_set, (d,i)->{})
                                    .show();
                            return true;
                        case R.id.action_delete_set:
                            builder = new AlertDialog.Builder(getActivity());
                            SpannableString title = new SpannableString(getString(R.string.want_to_delete));
                            title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                            builder.setTitle(title)
                                    .setPositiveButton(R.string.ok,(d,i)->{
                                        try {
                                            FlashCardSQLiteHelper.deleteCardSet(db, (int)id);
                                            Fragment fragment = new FlashCardSetFragment();
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.add(R.id.content_frame, fragment);
                                            ft.commit();
                                            Toast.makeText(getContext(), R.string.complete, Toast.LENGTH_SHORT);
                                        } catch (SQLException e) {
                                            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel_set, (d,i)->{})
                                    .show();
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
                return true;
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