package com.example.flashcard.method;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.example.flashcard.service.APICard;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;
import com.example.flashcard.service.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static Map<String, File> mapFiles = new HashMap<>();
    MediaPlayer mediaPlayer;


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
            if (mediaPlayer!=null && mediaPlayer.isPlaying())
            {
                mediaPlayer.release();
                mediaPlayer = null;
            }
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
            APICard apiCard = retrofit.create(APICard.class);
            APICardSet api = retrofit.create(APICardSet.class);
            List<String> files = new ArrayList<>();
            new Thread(()->{
                CountDownLatch latch = new CountDownLatch(1);
                try {
                   List<CardDTO> listCards = api.getById(Long.parseLong(String.valueOf(cardsetId))).execute().body().getCards();
                   cards = new Card[listCards.size()];
                   for(int i = 0;i<cards.length;++i)
                       cards[i] = new Card(listCards.get(i));
                    listCards.parallelStream().forEach(card->{
                        if (card.getFrontImage() !=null)
                            files.add(card.getFrontImage());
                        if (card.getFrontSound() !=null)
                            files.add(card.getFrontSound());
                        if (card.getBackImage() !=null)
                            files.add(card.getBackImage());
                        if (card.getBackSound() !=null)
                            files.add(card.getBackSound());
                    });

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
                files.parallelStream().forEach(file-> {
                    try {
                        mapFiles.put(file, Utils.saveFile(view.getContext(),apiCard.download(file).execute().body(),file));
                    } catch (Exception e) {e.printStackTrace();}
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
                    if (mediaPlayer!=null && mediaPlayer.isPlaying())
                    {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

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
                if (mediaPlayer!=null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

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
            imageView.setImageBitmap(null);
            String file;
            if (isFront) file = cards[cardIndex].getFrontImage();
            else file =  cards[cardIndex].getBackImage();
            if (file != null)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(mapFiles.get(file).getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
//            imageView.setImageResource(R.drawable.flashcard);

            if(imageView.getVisibility() != View.VISIBLE)
                imageView.setVisibility(View.VISIBLE);
            else
                imageView.setVisibility(View.INVISIBLE);
        });
        ImageButton btnSound = layout.findViewById(R.id.btnSound);
        btnSound.setOnClickListener(view -> {
            String file;
            if (isFront) file = cards[cardIndex].getFrontSound();
            else file= cards[cardIndex].getBackSound();
            if (file != null)
            {
                try {
                    if(mediaPlayer== null) {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(mapFiles.get(file).getAbsolutePath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                    else if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    else mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (cardIndex ==0) buttonPrevious.setVisibility(View.INVISIBLE);
        if (cardIndex==cards.length-1 || cards.length==1) buttonNext.setVisibility(View.INVISIBLE);
        if (cardIndex>0 && cardIndex <cards.length-1) {
            buttonPrevious.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        for(Map.Entry<String,File> file : mapFiles.entrySet()) {
            if (file.getValue().exists())
            {
                file.getValue().delete();
                Log.e("Đã xóa ",file.getValue().getAbsolutePath() );
            }
            else Log.e("Chưa xóa ", file.getValue().getAbsolutePath());
        }
        mapFiles = new HashMap<>();

        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.release();
            mediaPlayer = null;
        }


    }

}