package com.example.flashcard.method;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.adapter.CardAdapter;
import com.example.flashcard.data.Card;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.dto.CardSetDTO;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListCardFragment extends Fragment {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    private static int cardsetId=0;
    private static Card[] cards;
    private SQLiteDatabase db;
    private Cursor cardsCursor;
    List<CardDTO> dataList = new ArrayList<>();
    CardAdapter adapter;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = getActivity().getSharedPreferences("USER", MODE_PRIVATE);

        if (getArguments() != null) {
            cardsetId = getArguments().getInt("cardsetId", 0);
        }

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_list_card, container, false);
         listView = layout.findViewById(R.id.list_cards);
        setupListView(layout, listView);
        return layout;
    }

    public void filterList(String query) {
        if (adapter == null || dataList == null) return;

        List<CardDTO> filteredList = new ArrayList<>();
        for (CardDTO item : dataList) {
            if (item.getFrontText().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (query.isEmpty()) { // Nếu SearchView trống, hiển thị lại toàn bộ danh sách
            filteredList = new ArrayList<>(dataList);
        }

        adapter = new CardAdapter(getContext(), filteredList);
        listView.setAdapter(adapter); // Gán lại Adapter cho ListView
        adapter.notifyDataSetChanged();
    }

    private void setupListView(View view, ListView listView) {
        //Populate the list_set ListView from a cursor

        if (user != null) {
            APICardSet api = retrofit.create(APICardSet.class);
            api.getById(Long.parseLong(String.valueOf(cardsetId))).enqueue(new Callback<CardSetDTO>() {
                @Override
                public void onResponse(Call<CardSetDTO> call, Response<CardSetDTO> response) {
                    if (response.isSuccessful()) {
                        CardSetDTO cardSetDTO = response.body();
                         dataList = cardSetDTO.getCards();
                         adapter = new CardAdapter(view.getContext(),dataList);
                        listView.setAdapter(adapter);
                    } else
                        Toast.makeText(view.getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<CardSetDTO> call, Throwable t) {

                }
            });
        } else {
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
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                if (user !=null) {
                    CardDTO cardDTO = (CardDTO) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), UpdateCardActivity.class);
                    intent.putExtra( UpdateCardActivity.EXTRA_CARDID, String.valueOf(cardDTO.getId()));
                    intent.putExtra(UpdateCardActivity.EXTRA_CARDSETID, String.valueOf(cardsetId));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), UpdateCardActivity.class);
                    intent.putExtra( UpdateCardActivity.EXTRA_CARDID, String.valueOf(id));
                    intent.putExtra(UpdateCardActivity.EXTRA_CARDSETID, String.valueOf(cardsetId));
                    startActivity(intent);
                }

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