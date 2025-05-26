package com.example.flashcard.drawer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcard.MainActivity;
import com.example.flashcard.R;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.AccountUserDTO;
import com.example.flashcard.method.SignUpActivity;
import com.example.flashcard.service.APIAccountUser;
import com.example.flashcard.service.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogInActivity extends AppCompatActivity {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    private SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user= getSharedPreferences("USER", MODE_PRIVATE);
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_log_in);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupButton();
    }
    private void setupButton() {
        Button btn_sign_up = (Button) findViewById(R.id.link_sign_up);
        EditText editText_UserName = (EditText)findViewById(R.id.account_name);
        EditText editText_Password = (EditText) findViewById(R.id.account_password);
        btn_sign_up.setOnClickListener(v->{
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        Button btn_log_in = (Button) findViewById(R.id.btn_log_in);
        btn_log_in.setOnClickListener(v->{
            System.out.println("Test");

            String username = editText_UserName.getText().toString();
            String password = editText_Password.getText().toString();

            APIAccountUser api = retrofit.create(APIAccountUser.class);

            AccountUserDTO loginRequest = new AccountUserDTO();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            api.login(loginRequest).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_SHORT).show();
                        JsonObject message = response.body();
                        // Xử lý thông báo thành công
                        user
                                .edit()
                                .putString("username", message.get("username").getAsString())
                                .putLong("id",message.get("id").getAsLong())
                                .apply();
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Xử lý lỗi đăng nhập
                        Snackbar.make(findViewById(android.R.id.content), "Failed", Snackbar.LENGTH_SHORT).show();
                        editText_UserName.setText("");
                        editText_Password.setText("");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    // Xử lý lỗi kết nối
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}