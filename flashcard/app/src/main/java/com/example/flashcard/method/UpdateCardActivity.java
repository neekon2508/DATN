package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.service.APICard;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;
import com.example.flashcard.service.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.CountDownLatch;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateCardActivity extends AppCompatActivity {

    private CardDTO cardDTO = new CardDTO();
    public static final String EXTRA_CARDID = "cardId";
    public static int CARDID = 0;

    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;
    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getSharedPreferences("USER", MODE_PRIVATE);

        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_card);

        CARDID = Integer.parseInt(getIntent().getStringExtra(EXTRA_CARDID));
        CARDSETID = Integer.parseInt(getIntent().getStringExtra(EXTRA_CARDSETID));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_card);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupCardView();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ListCardActivity.class);
                intent.putExtra(ListCardActivity.EXTRA_CARDSETID, String.valueOf(CARDSETID));
                finish();
                startActivity(intent);

            case R.id.action_more_vert:
                return true;
            case R.id.action_create_card:
                EditText editFrontText = (EditText) findViewById(R.id.update_front_text);
                String frontText = editFrontText.getText().toString();
                EditText editBackText = (EditText) findViewById(R.id.update_back_text);
                String backText = editBackText.getText().toString();
                if (!frontText.isEmpty() && !backText.isEmpty()) {
                    if (user != null) {
                        APICard apiCard = retrofit.create(APICard.class);
                        MultipartBody.Part frontImagePart = Utils.getMultipartFromUri(getApplicationContext(),Utils.frontImageUri,"file");
                        MultipartBody.Part frontSoundPart = Utils.getMultipartFromUri(getApplicationContext(),Utils.frontSoundUri,"file");
                        MultipartBody.Part backImagePart = Utils.getMultipartFromUri(getApplicationContext(),Utils.backImageUri,"file");
                        MultipartBody.Part backSoundPart = Utils.getMultipartFromUri(getApplicationContext(),Utils.backSoundUri,"file");

                        new Thread(() -> {
                            CountDownLatch latch = new CountDownLatch(1);
                            try {
                                if (frontImagePart != null)
                                    cardDTO.setFrontImage(apiCard.upload(frontImagePart).execute().body().get("filePath").getAsString());
                                if (frontSoundPart != null)
                                    cardDTO.setFrontSound(apiCard.upload(frontSoundPart).execute().body().get("filePath").getAsString());
                                if (backImagePart != null)
                                    cardDTO.setBackImage(apiCard.upload(backImagePart).execute().body().get("filePath").getAsString());
                                if (backSoundPart != null)
                                    cardDTO.setBackSound(apiCard.upload(backSoundPart).execute().body().get("filePath").getAsString());
                                cardDTO.setFrontText(frontText);
                                cardDTO.setBackText(backText);

                                apiCard.update(Long.valueOf((long)CARDID),cardDTO).execute();

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                            try {
                                latch.await();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           runOnUiThread(() -> {
                              Intent intent1 = new Intent(this, ListCardActivity.class);
                               intent1.putExtra(ListCardActivity.EXTRA_CARDSETID, String.valueOf(CARDSETID));
                               startActivity(intent1);
                            });
                        }).start();

                    } else {
                        try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
                             SQLiteDatabase db = flashCardSQLiteHelper.getWritableDatabase()) {
                            FlashCardSQLiteHelper.updateCard(db, CARDID, frontText, backText);
                            intent = new Intent(this, ListCardActivity.class);
                            intent.putExtra(ListCardActivity.EXTRA_CARDSETID, String.valueOf(CARDSETID));
                            startActivity(intent);
                        }
                    }
                    }
                else
                    Toast.makeText(this,R.string.data_empty, Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupCardView() {
        EditText frontText = (EditText) findViewById(R.id.update_front_text);
        EditText backText = (EditText) findViewById(R.id.update_back_text);
        if (user != null) {
            APICard api = retrofit.create(APICard.class);
            api.getById(Long.parseLong(String.valueOf(CARDID))).enqueue(new Callback<CardDTO>() {
                @Override
                public void onResponse(Call<CardDTO> call, Response<CardDTO> response) {
                    if (response.isSuccessful()) {
                        CardDTO cardDTO = response.body();
                        frontText.setText(cardDTO.getFrontText());
                        backText.setText(cardDTO.getBackText());
                    }
                }

                @Override
                public void onFailure(Call<CardDTO> call, Throwable t) {

                }
            });
        } else {
            try(FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
                SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();
                Cursor cardCursor = db.query("CARD", new String[] {"FRONTTEXT","BACKTEXT"},
                        "_id=?", new String[] {Integer.toString((int)CARDID)}, null, null, null);) {
                if (cardCursor.moveToFirst()) {
                    frontText.setText(cardCursor.getString(0));
                    backText.setText(cardCursor.getString(1));
                }

            } catch (SQLException e) {
                Toast.makeText(this,R.string.data_empty, Toast.LENGTH_SHORT);
            }
        }

    }
    private void setupButton() {
        ImageButton btnImageFrontUpload = findViewById(R.id.btnUpdateImageFrontUpload);
        btnImageFrontUpload.setOnClickListener(view -> openFileChooser("image/*", Utils.PICK_IMAGE_FRONT,btnImageFrontUpload));

        ImageButton btnSoundFrontUpload = findViewById(R.id.btnUpdateSoundFrontUpload);
        btnSoundFrontUpload.setOnClickListener(view -> openFileChooser("audio/*", Utils.PICK_AUDIO_FRONT,btnSoundFrontUpload));
//
        ImageButton btnImageBackUpload = findViewById(R.id.btnUpdateImageBackUpload);
        btnImageBackUpload.setOnClickListener(view -> openFileChooser("image/*", Utils.PICK_IMAGE_BACK,btnImageBackUpload));
//
        ImageButton btnSoundBackUpload = findViewById(R.id.btnUpdateSoundBackUpload);
        btnSoundBackUpload.setOnClickListener(view -> openFileChooser("audio/*", Utils.PICK_AUDIO_BACK,btnSoundBackUpload));
    }

    private void openFileChooser(String fileType, int requestCode, ImageButton button) {
        button.setBackgroundColor(Color.BLUE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(fileType);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
            Utils.frontImageUri = data.getData();
        if (requestCode == 2 && resultCode == RESULT_OK && data != null)
            Utils.frontSoundUri = data.getData();
        if (requestCode == 3 && resultCode == RESULT_OK && data != null)
            Utils.backImageUri = data.getData();
        if (requestCode == 4 && resultCode == RESULT_OK && data != null)
            Utils.backSoundUri = data.getData();
    }

}