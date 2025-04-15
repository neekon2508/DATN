package com.example.flashcard.method;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.Card;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.google.android.material.snackbar.Snackbar;


public class LearnFragment extends Fragment {

    private AnimatorSet front_anim;
    private AnimatorSet back_anim;
    private boolean isFront = true;
    private static int cardsetId=0;
    private static int cardIndex=0;
    private static Card[] cards;
    private SQLiteDatabase db;
    private Cursor cardsCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            cardsetId = getArguments().getInt("cardsetId", 0);
        }

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_learn, container, false);
        setupCardArray(layout);
        setupAnimator(layout);


        setupButton(layout);
        return layout;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//    }
    private void setupAnimator(View layout) {
        float scale = layout.getContext().getResources().getDisplayMetrics().density;
        TextView card_front = (TextView) layout.findViewById(R.id.front_text);
        TextView card_back = (TextView) layout.findViewById(R.id.back_text);
        card_front.setCameraDistance(8000 * scale);
        card_back.setCameraDistance(8000 * scale);

        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(layout.getContext(), R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(layout.getContext(), R.animator.back_animator);

        Button check_btn = (Button) layout.findViewById(R.id.check);
        check_btn.setOnClickListener(v->{
            if(isFront) {
                front_anim.setTarget(card_front);
                back_anim.setTarget(card_back);
                front_anim.start();
                back_anim.start();
                isFront = false;
            } else {
                front_anim.setTarget(card_back);
                back_anim.setTarget(card_front);
                back_anim.start();
                front_anim.start();
                isFront = true;
            }
        });
    }

    private void setupCardArray(View view) {

        try {
            FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
            db = flashCardSQLiteHelper.getReadableDatabase();
            cardsCursor = db.query("CARD", new String[] {"_id", "FRONTTEXT","BACKTEXT"},
                    "CARDSETID=?", new String[] {Integer.toString((int)cardsetId)}, null, null, null);
            if( cardsCursor != null && cardsCursor.moveToFirst()) {
                cards = new Card[cardsCursor.getCount()];
                for(int i =0; i< cards.length; ++i) {
                    cards[i] = new Card(cardsCursor.getInt(0),
                                    cardsCursor.getString(1),
                                    cardsCursor.getString(2));
                    cardsCursor.moveToNext();
                }
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupButton(View layout) {
        TextView frontText = (TextView) layout.findViewById(R.id.front_text);
        TextView backText = (TextView) layout.findViewById(R.id.back_text);

        frontText.setText(cards[cardIndex].getFrontText());
        backText.setText(cards[cardIndex].getBackText());

        Button buttonPrevious = (Button) layout.findViewById(R.id.button_previous);
        Button buttonNext = (Button) layout.findViewById(R.id.button_next);
        buttonPrevious.setOnClickListener(l-> {
            if(cardIndex > 1) cardIndex--;
            else Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        });
        buttonNext.setOnClickListener(l->{
            if(cardIndex < cards.length) cardIndex++;
            else Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        });
    }
}