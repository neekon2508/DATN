package com.example.flashcard.drawer;

import android.content.Intent;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class StatisticFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_statistic, container, false);
        setupPieChart(layout);
        return layout;
    }

    private void setupPieChart(View view) {
        int isLearned = 0;
        int totalCards = 1;
        int learnedCount=0;

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
        }
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