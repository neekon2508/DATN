package com.example.flashcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.anychart.ui.contextmenu.Item;
import com.example.flashcard.dto.CardSetDTO;

import java.util.List;

public class CardSetAdapter extends ArrayAdapter<CardSetDTO> {

    public CardSetAdapter(@NonNull Context context, List<CardSetDTO> cardSets) {
        super(context, 0, cardSets);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        CardSetDTO cardSetDTO = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(cardSetDTO.getName());
        return convertView;
    }
}
