package com.example.flashcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.dto.CardSetDTO;

import java.util.List;

public class CardAdapter extends ArrayAdapter<CardDTO> {

    public CardAdapter(@NonNull Context context, List<CardDTO> cards) {
        super(context, 0, cards);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        CardDTO cardDTO = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(cardDTO.getFrontText());
        return convertView;
    }
}