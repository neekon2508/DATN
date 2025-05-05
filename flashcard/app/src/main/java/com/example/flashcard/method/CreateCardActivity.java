package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.CardDTO;
import com.example.flashcard.dto.CardSetDTO;
import com.example.flashcard.service.APICard;
import com.example.flashcard.service.APICardSet;
import com.example.flashcard.service.RetrofitClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateCardActivity extends AppCompatActivity {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;
    private static final int PICK_IMAGE = 1;
    private Uri frontImageUri;
    private CardDTO cardDTO = new CardDTO();

    SharedPreferences user;
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

        Button btnFrontImageUpload = (Button) findViewById(R.id.btnImageFrontUpload);
        btnFrontImageUpload.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, PICK_IMAGE);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            frontImageUri = data.getData();
        }
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
                        MultipartBody.Part frontImagePart = prepareFilePart("file",frontImageUri, getApplicationContext());
                        if (frontImagePart != null)
                            apiCard.upload(frontImagePart).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful()) {
                                        System.out.println(response.body());
                                        cardDTO.setFrontImage(response.body());
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),"Tạo file ko thành công",Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"Lỗi khi tạo request",Toast.LENGTH_SHORT).show();

                                }
                            });

                        cardDTO.setFrontText(frontText);
                        cardDTO.setBackText(backText);
                        apiCardSet.createCard(Long.parseLong(String.valueOf(CARDSETID)), cardDTO).enqueue(new Callback<CardSetDTO>() {
                            @Override
                            public void onResponse(Call<CardSetDTO> call, Response<CardSetDTO> response) {
                                if (response.isSuccessful())
                                    Toast.makeText(getApplicationContext(), R.string.complete, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<CardSetDTO> call, Throwable t) {

                            }
                        });
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
    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Context context) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            RequestBody requestBody = new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse("image/*");
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    Source source = Okio.source(inputStream);
                    sink.writeAll(source);
                }
            };
            String fileName = getFileName(context, fileUri);
            return MultipartBody.Part.createFormData(partName, fileName, requestBody);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                result = cursor.getString(index);
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
