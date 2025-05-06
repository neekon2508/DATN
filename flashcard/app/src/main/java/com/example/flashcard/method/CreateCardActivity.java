package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.dto.CardSetDTO;
import com.example.flashcard.service.APICard;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;
import com.example.flashcard.service.Utils;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateCardActivity extends AppCompatActivity {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;

    private CardDTO cardDTO = new CardDTO();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);

        user = getSharedPreferences("USER", MODE_PRIVATE);

        setContentView(R.layout.activity_create_card);
        Intent intent = getIntent();
        CARDSETID = Integer.parseInt(intent.getStringExtra(EXTRA_CARDSETID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_card);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setupButton();

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
//            frontImageUri = data.getData();
//        }
//    }
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

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more_vert:
                return true;
            case R.id.action_create_card:
                EditText editFrontText = (EditText) findViewById(R.id.front_text);
                String frontText = editFrontText.getText().toString();
                EditText editBackText = (EditText) findViewById(R.id.back_text);
                String backText = editBackText.getText().toString();

                if (!frontText.isEmpty() && !backText.isEmpty()) {
                    if (user != null) {
                        APICardSet apiCardSet = retrofit.create(APICardSet.class);
                        APICard apiCard = retrofit.create(APICard.class);
                        MultipartBody.Part frontImagePart = Utils.getMultipartFromUri(getApplicationContext(),Utils.frontImageUri,"file");
                        MultipartBody.Part frontSoundPart = Utils.getMultipartFromUri(getApplicationContext(),Utils.frontSoundUri,"file");
                        MultipartBody.Part backImagePart = Utils.getMultipartFromUri(getApplicationContext(),Utils.backImageUri,"file");
                        MultipartBody.Part backSoundPart = Utils.getMultipartFromUri(getApplicationContext(),Utils.backSoundUri,"file");

                        new Thread(() -> {
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

                                apiCardSet.createCard(Long.parseLong(String.valueOf(CARDSETID)), cardDTO).execute();
                                editFrontText.setText("");
                                editBackText.setText("");
                                reset();
                                Toast.makeText(this, R.string.complete, Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        }



                    else {
                        try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
                             SQLiteDatabase db = flashCardSQLiteHelper.getWritableDatabase()) {

                            if(CARDSETID != 0)
                            {
                                FlashCardSQLiteHelper.insertCard(db, CARDSETID, frontText, backText);
                                Toast.makeText(this, R.string.complete, Toast.LENGTH_SHORT).show();

                            }
                            else
                                Toast.makeText(this, R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();
                    }
                        editFrontText.setText("");
                        editBackText.setText("");
                    }
                }
                else
                    Toast.makeText(this,R.string.data_empty, Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset() {
        ImageButton btnImageFrontUpload = findViewById(R.id.btnImageFrontUpload);btnImageFrontUpload.setBackgroundColor(Color.GRAY);
        ImageButton btnSoundFrontUpload = findViewById(R.id.btnSoundFrontUpload);btnSoundFrontUpload.setBackgroundColor(Color.GRAY);
        ImageButton btnImageBackUpload = findViewById(R.id.btnImageBackUpload);btnImageBackUpload.setBackgroundColor(Color.GRAY);
        ImageButton btnSoundBackUpload = findViewById(R.id.btnSoundBackUpload);btnSoundBackUpload.setBackgroundColor(Color.GRAY);
    }
    private void setupButton() {
        ImageButton btnImageFrontUpload = findViewById(R.id.btnImageFrontUpload);
        btnImageFrontUpload.setOnClickListener(view -> openFileChooser("image/*", Utils.PICK_IMAGE_FRONT,btnImageFrontUpload));

        ImageButton btnSoundFrontUpload = findViewById(R.id.btnSoundFrontUpload);
        btnSoundFrontUpload.setOnClickListener(view -> openFileChooser("audio/*", Utils.PICK_AUDIO_FRONT,btnSoundFrontUpload));
//
        ImageButton btnImageBackUpload = findViewById(R.id.btnImageBackUpload);
        btnImageBackUpload.setOnClickListener(view -> openFileChooser("image/*", Utils.PICK_IMAGE_BACK,btnImageBackUpload));
//
        ImageButton btnSoundBackUpload = findViewById(R.id.btnSoundBackUpload);
        btnSoundBackUpload.setOnClickListener(view -> openFileChooser("audio/*", Utils.PICK_AUDIO_BACK,btnSoundBackUpload));
    }

    private void openFileChooser(String fileType, int requestCode, ImageButton button) {
        button.setBackgroundColor(Color.BLUE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(fileType);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, requestCode);
    }
}

