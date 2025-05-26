package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.MySQLConnector;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.dto.AccountUserDTO;
import com.example.flashcard.service.APIAccountUser;
import com.example.flashcard.service.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button sign_up_btn = (Button) findViewById(R.id.sign_up);
        sign_up_btn.setOnClickListener(v->setup());
    }

    private void setup() {
        EditText newUserNameBtn = (EditText) findViewById(R.id.new_account_name);
        EditText newUserPasswordBtn = (EditText) findViewById(R.id.new_account_password);

        String newUserName = newUserNameBtn.getText().toString();
        String newUserPass = newUserPasswordBtn.getText().toString();

        if (!newUserName.isEmpty() && !newUserPass.isEmpty()) {
            APIAccountUser api = retrofit.create(APIAccountUser.class);
            api.createAccountUser(new AccountUserDTO(newUserName,newUserPass,"ROLE_USER")).enqueue(new Callback<AccountUserDTO>() {
                @Override
                public void onResponse(Call<AccountUserDTO> call, Response<AccountUserDTO> response) {
                    if (response.isSuccessful())
                    {
                        Snackbar.make(findViewById(android.R.id.content), "Success!", Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.account_exist, Snackbar.LENGTH_SHORT).show();
                        newUserNameBtn.setText("");
                        newUserPasswordBtn.setText("");
                    }
                }

                @Override
                public void onFailure(Call<AccountUserDTO> call, Throwable t) {

                }
            });

        }
    }
}