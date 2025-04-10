package com.example.flashcard.drawer;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flashcard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FlashCardSetFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_flash_card_set, container, false);
        FloatingActionButton fab = layout.findViewById(R.id.createSet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return layout;
    }

    private void onClickDone() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createSet:
                onClickDone();
                break;
        }
    }
}