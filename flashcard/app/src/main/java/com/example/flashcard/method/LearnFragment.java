package com.example.flashcard.method;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.Card;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Retrofit;


public class LearnFragment extends Fragment {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    private AnimatorSet front_anim;
    private AnimatorSet back_anim;
    private boolean isFront = true;
    private static int cardsetId=0;
    private static int cardIndex=0;
    private static Card[] cards;
    private SQLiteDatabase db;
    private Cursor cardsCursor;
    private boolean firstCheck = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = getActivity().getSharedPreferences("USER", MODE_PRIVATE);

        if (getArguments() != null) {
            cardsetId = getArguments().getInt("cardsetId", 0);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            cardsetId = bundle.getInt("cardsetId", 0);
            cardIndex = bundle.getInt("cardIndex", 0);
        }


        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_learn, container, false);
        setupCardArray(layout);

        return layout;
    }

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
            try(FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(getContext());
                SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase()) {
                if (!firstCheck) {
                    FlashCardSQLiteHelper.updateCardByLearned(db, cards[cardIndex].getId(), 1);
                    firstCheck = true;
                }
                FlashCardSQLiteHelper.updateCardByLearnedAt(db, cardIndex);
            }
        });
    }

    private void setupCardArray(View view) {

        if (user != null)
        {
            APICardSet api = retrofit.create(APICardSet.class);
            new Thread(()->{
                CountDownLatch latch = new CountDownLatch(1);
                try {
                   List<CardDTO> listCards = api.getById(Long.parseLong(String.valueOf(cardsetId))).execute().body().getCards();
                   cards = new Card[listCards.size()];
                   for(int i = 0;i<cards.length;++i)
                       cards[i] = new Card(listCards.get(i));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
                try {
                    latch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(() -> {
                    setupButton(view);
                    setupAnimator(view);
                });

            }).start();
        }
        else {
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
            setupAnimator(view);


            setupButton(view);
            } catch (SQLException e) {
                Toast.makeText(getContext(), R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupButton(View layout) {
        TextView frontText = (TextView) layout.findViewById(R.id.front_text);
        TextView backText = (TextView) layout.findViewById(R.id.back_text);


            frontText.setText(cards[cardIndex].getFrontText());
            backText.setText(cards[cardIndex].getBackText());



        Bundle bundle = new Bundle();
        bundle.putInt("cardsetId", cardsetId);

        LearnFragment fragment = new LearnFragment();



        Button buttonPrevious = (Button) layout.findViewById(R.id.button_previous);
        Button buttonNext = (Button) layout.findViewById(R.id.button_next);
        buttonPrevious.setOnClickListener(l-> {
            if(cardIndex > 0)
                {
                    cardIndex--;
                    bundle.putInt("cardIndex", cardIndex);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            else Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        });
        buttonNext.setOnClickListener(l->{
            if(cardIndex < cards.length-1)
            {
                cardIndex++;
                bundle.putInt("cardIndex", cardIndex);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            else Toast.makeText(getContext(),R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
        });
        ImageButton btnImage = layout.findViewById(R.id.btnImage);
        ImageView imageView = layout.findViewById(R.id.imageViewDisplay);
        btnImage.setOnClickListener(view -> {
            imageView.setImageResource(R.drawable.flashcard);
            if(imageView.getVisibility() != View.VISIBLE)
                imageView.setVisibility(View.VISIBLE);
            else
                imageView.setVisibility(View.INVISIBLE);
        });
    }
}