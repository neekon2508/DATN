package com.example.flashcard.drawer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.flashcard.R;

import java.util.ArrayList;
import java.util.List;


public class StatisticFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_statistic, container, false);

        AnyChartView anyChartView = layout.findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Apples", 6371664));
        data.add(new ValueDataEntry("Pears", 789622));
        data.add(new ValueDataEntry("Bananas", 7216301));
        pie.data(data);

        anyChartView.setChart(pie);
        return layout;
    }

    private void setupPieChart(View view) {
//        int isLearned = 0;
//        int totalCards = 1;
//        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
//        List<DataEntry> entries = new ArrayList<>();
//        try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
//             SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();)
//        {
//            Cursor cardsCursor = db.query("CARD", new String[] {"_id"},
//                    "ISLEARNED=?", new String[] {Integer.toString((int)1)}, null, null, null);
//            isLearned = cardsCursor.getCount();
//            cardsCursor = db.query("CARD", new String[] {"_id"},
//                    null, null, null, null, null);
//            totalCards = cardsCursor.getCount();
//        }
//        if(totalCards > 0) {
//            entries.add(new PieEntry(isLearned, "Learned"));
//            entries.add(new PieEntry(totalCards-isLearned,"Not Learned"));
//
//            PieDataSet pieDataSet = new PieDataSet(entries, "Study");
//            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//            PieData pieData = new PieData(pieDataSet);
//            pieChart.setData(pieData);
//            pieChart.getDescription().setEnabled(false);
//            pieChart.animateY(1000);
//            pieChart.invalidate();
//        }

    }
}