package com.example.flashcard.drawer;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.service.APICard;
import com.example.flashcard.service.RetrofitClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class StatisticFragment extends Fragment {
    long isLearned;
    long totalCards;
    long learnedCount;
    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = getActivity().getSharedPreferences("USER", MODE_PRIVATE);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_statistic, container, false);
        setupPieChart(layout);
        return layout;
    }

    private void setupPieChart(View view) {
        APICard apiCard = retrofit.create(APICard.class);

        if (user != null) {
            apiCard.allCards().enqueue(new Callback<List<CardDTO>>() {
                @Override
                public void onResponse(Call<List<CardDTO>> call, Response<List<CardDTO>> response) {
                    if (response.isSuccessful()) {
                        List<CardDTO> allCards = response.body();
                         totalCards = allCards.size();
                         isLearned = allCards.parallelStream().filter(card-> card.getLearned()).count();
                         learnedCount =allCards.parallelStream().filter(card->
                                card.getLearned()
                                && LocalDateTime.parse(card.getUpdatedAt()).toLocalDate().equals(LocalDate.now())).count();
                         viewPieChart(view);
                    }
                }

                @Override
                public void onFailure(Call<List<CardDTO>> call, Throwable t) {

                }
            });
        } else {
            try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
                 SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();)
            {
                Cursor cardsCursor = db.query("CARD", new String[] {"_id"},
                        "ISLEARNED=?", new String[] {Integer.toString((int)1)}, null, null, null);
                isLearned = cardsCursor.getCount();
                cardsCursor = db.query("CARD", new String[] {"_id"},
                        null, null, null, null, null);
                totalCards = cardsCursor.getCount();

                cardsCursor = db.query("CARD", new String[] {"LEARNEDAT"}, "ISLEARNED=? AND LEARNEDAT LIKE ?",
                        new String[] {String.valueOf(1), LocalDate.now().toString()+"%"},null,null,null);
                learnedCount = cardsCursor.getCount();
                viewPieChart(view);
            }


        }


    }
    private void viewPieChart(View view) {
        if(totalCards > 0) {
            AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
            List<DataEntry> entries = new ArrayList<>();
            Pie pie = AnyChart.pie();
            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Learned", isLearned));
            data.add(new ValueDataEntry("Not Learned", totalCards-isLearned));
            pie.data(data);
            anyChartView.setChart(pie);
        }
        TextView textLearnedCount = (TextView) view.findViewById(R.id.learned_count);
        textLearnedCount.setText(String.valueOf(getResources().getString(R.string.learned)+
                " "+learnedCount+" "+
                getResources().getString(R.string.words)));
    }
}