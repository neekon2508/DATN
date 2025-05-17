package com.example.flashcard.drawer;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
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
import com.example.flashcard.adapter.CardAdapter;
import com.example.flashcard.adapter.CardSetAdapter;
import com.example.flashcard.data.FlashCardSQLiteHelper;

import com.example.flashcard.data.LocaleHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.AccountUserDTO;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.dto.CardSetDTO;
import com.example.flashcard.method.CreateCardActivity;
import com.example.flashcard.method.LearnActivity;
import com.example.flashcard.method.ListCardActivity;
import com.example.flashcard.service.APIAccountUser;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FlashCardSetFragment extends Fragment implements View.OnClickListener{
    private SQLiteDatabase db;
    private Cursor setsCursor;
    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    List<CardSetDTO> dataList = new ArrayList<>();
    CardSetAdapter adapter;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ThemeManager.setTheme(getContext());
        user = getActivity().getSharedPreferences("USER", MODE_PRIVATE);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_flash_card_set, container, false);
        LocaleHelper.setAppLocale(layout.getContext());
        FloatingActionButton createSet = layout.findViewById(R.id.createSet);
        createSet.setOnClickListener(this);
        setupListView(layout);
        return layout;
    }
    private void onClickDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        APIAccountUser api = retrofit.create(APIAccountUser.class);
        EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        builder.setTitle(R.string.create_set)
                .setView(editText)
                .setPositiveButton(R.string.ok,(d,i)->{
                    String setName = editText.getText().toString();
                    if (user != null) {
                        Long id = user.getLong("id",0);
                        CardSetDTO newCardSet = new CardSetDTO();
                        newCardSet.setName(setName);
                        api.createCardSet(id, newCardSet).enqueue(new Callback<AccountUserDTO>() {
                            @Override
                            public void onResponse(Call<AccountUserDTO> call, Response<AccountUserDTO> response) {
                                if (response.isSuccessful())
                                {
                                    Fragment fragment = new FlashCardSetFragment();
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.add(R.id.content_frame, fragment);
                                    ft.commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<AccountUserDTO> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        try {
                            FlashCardSQLiteHelper.insertCardSet(db, setName);
                            Fragment fragment = new FlashCardSetFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.add(R.id.content_frame, fragment);
                            ft.commit();
                        } catch (SQLException e) {
                            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                        }
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
         listView = view.findViewById(R.id.list_sets);
       if (user.getString("username",null)!=null) {
           APIAccountUser api = retrofit.create(APIAccountUser.class);
           api.getById(user.getLong("id",0)).enqueue(new Callback<AccountUserDTO>() {
               @Override
               public void onResponse(Call<AccountUserDTO> call, Response<AccountUserDTO> response) {
                   if (response.isSuccessful()) {
                       AccountUserDTO accountUserDTO = response.body();
                       dataList = accountUserDTO.getCardSets();
                       adapter= new CardSetAdapter(
                               view.getContext(),
                               dataList
                       );
                       listView.setAdapter(adapter);

                   } else
                       Toast.makeText(view.getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onFailure(Call<AccountUserDTO> call, Throwable t) {
                   Toast.makeText(view.getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
               }
           });
       }
       else {
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
       }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                if (user.getString("username",null)!=null) {
                    CardSetDTO cardSetDTO = (CardSetDTO) listView.getItemAtPosition(position);
                    if (!cardSetDTO.getCards().isEmpty()) {
                        Intent intent = new Intent(getActivity(), LearnActivity.class);
                        intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(cardSetDTO.getId()));
                        startActivity(intent);
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_set, Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.add, t -> {
                            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                            intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(cardSetDTO.getId()));
                            startActivity(intent);
                        });
                        snackbar.show();
                    }
                }
                else {
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

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
                APICardSet api = retrofit.create(APICardSet.class);
                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.getMenuInflater().inflate(R.menu.menu_card_set_popup, popup.getMenu());
                popup.setGravity(Gravity.CENTER);
                popup.setOnMenuItemClickListener(item -> {
                    switch(item.getItemId()) {
                        case R.id.action_add_card:
                            CardSetDTO cardSetDTO = (CardSetDTO) listView.getItemAtPosition(position);
                            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
                            intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(cardSetDTO.getId()));
                            startActivity(intent);
                            return true;
                        case R.id.action_see_all_card:
                            if (user.getString("username",null)!=null) {
                                cardSetDTO = (CardSetDTO) listView.getItemAtPosition(position);
                                if (!cardSetDTO.getCards().isEmpty()) {
                                    intent = new Intent(getActivity(), ListCardActivity.class);
                                    intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(cardSetDTO.getId()));
                                    startActivity(intent);
                                }else {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_set, Snackbar.LENGTH_LONG);
                                    snackbar.setAction(R.string.add, t -> {
                                        Intent createIntent = new Intent(getActivity(), CreateCardActivity.class);
                                        createIntent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(cardSetDTO.getId()));
                                        startActivity(createIntent);
                                    });
                                    snackbar.show();
                                }
                                return true;
                            }
                            try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
                                 SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();
                                 Cursor cardsCursor = db.query("CARD", new String[] {"_id"},
                                         "CARDSETID=?", new String[] {Integer.toString((int)id)}, null, null, null);)
                            {
                                if (cardsCursor.moveToFirst()) {
                                    intent = new Intent(getActivity(), ListCardActivity.class);
                                    intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(id));
                                    startActivity(intent);
                                }
                                else {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.empty_set, Snackbar.LENGTH_LONG);
                                    snackbar.setAction(R.string.add, t -> {
                                        Intent createIntent = new Intent(getActivity(), CreateCardActivity.class);
                                        createIntent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(id));
                                        startActivity(createIntent);
                                    });
                                    snackbar.show();
                                }

                            } catch (SQLException e) {
                                Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                            }
                            return true;
                        case R.id.action_change_name_set:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            cardSetDTO = (CardSetDTO) listView.getItemAtPosition(position);
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
                                        if (user.getString("username",null)==null) {
                                            CardSetDTO update = new CardSetDTO();
                                            update.setName(changeSetName);

                                                new Thread(()->{
                                                    try {
                                                    api.update(cardSetDTO.getId(),update).execute();
                                                } catch (Exception e) {e.printStackTrace();}
                                                    }).start();
                                        }
                                        else {
                                            try {
                                                FlashCardSQLiteHelper.updateCardSet(db, (int)id, changeSetName);
                                            } catch (SQLException e) {
                                                Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                                            }
                                        }
                                        ((TextView)view).setText(changeSetName);
                                        Toast.makeText(getContext(), R.string.complete, Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton(R.string.cancel_set, (d,i)->{})
                                    .show();
                            return true;
                        case R.id.action_delete_set:
                            cardSetDTO = (CardSetDTO) listView.getItemAtPosition(position);
                            builder = new AlertDialog.Builder(getActivity());
                            SpannableString title = new SpannableString(getString(R.string.want_to_delete));
                            title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                            builder.setTitle(title)
                                    .setPositiveButton(R.string.ok,(d,i)->{
                                        if (user.getString("username",null)!=null) {
                                            new Thread(()->{
                                                try {
                                                    api.delete(cardSetDTO.getId()).execute();
                                                    Fragment fragment = new FlashCardSetFragment();
                                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                    ft.replace(R.id.content_frame, fragment);
                                                    ft.commit();
                                                } catch (Exception e) {e.printStackTrace();}
                                            }).start();
                                        } else {
                                            try {
                                                FlashCardSQLiteHelper.deleteCardSet(db, (int)id);
                                                Fragment fragment = new FlashCardSetFragment();
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();
                                            } catch (SQLException e) {
                                                Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT);
                                            }
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
public void filterList(String query) {
    if (adapter == null || dataList == null) return;

    List<CardSetDTO> filteredList = new ArrayList<>();
    for (CardSetDTO item : dataList) {
        if (item.getName().toLowerCase().contains(query.toLowerCase())) {
            filteredList.add(item);
        }
    }

    if (query.isEmpty()) { // Nếu SearchView trống, hiển thị lại toàn bộ danh sách
        filteredList = new ArrayList<>(dataList);
    }

    adapter = new CardSetAdapter(getContext(), filteredList);
    listView.setAdapter(adapter); // Gán lại Adapter cho ListView
    adapter.notifyDataSetChanged();
}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (setsCursor != null)
        {
            setsCursor.close();
            db.close();
        }

    }

//    private class UpdateSetTask extends AsyncTask<Integer, Void, Boolean> {
//
//        protected Boolean doInBackground(Integer... integers) {
//            return null;
//        }
//    }
}