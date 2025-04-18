package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.ThemeManager;

public class CreateCardActivity extends AppCompatActivity {

    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        Intent intent = getIntent();
        CARDSETID = Integer.parseInt(intent.getStringExtra(EXTRA_CARDSETID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_card);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                    try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
                         SQLiteDatabase db = flashCardSQLiteHelper.getWritableDatabase()) {

                        if(CARDSETID != 0)
                        {
                            FlashCardSQLiteHelper.insertCard(db, CARDSETID, frontText, backText);
                            Toast.makeText(this, R.string.complete, Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(this, R.string.data_unavailable_message, Toast.LENGTH_SHORT).show();

                        editFrontText.setText("");
                        editBackText.setText("");
                    }
                }
                else
                    Toast.makeText(this,R.string.data_empty, Toast.LENGTH_SHORT);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}