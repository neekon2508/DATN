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

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

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
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute( ()->{
                        try(MySQLConnector sql = new MySQLConnector();)
                        {
                            sql.signUp(newUserName, newUserPass);
                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }

            );

        }
    }
}