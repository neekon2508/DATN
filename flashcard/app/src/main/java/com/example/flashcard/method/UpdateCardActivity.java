package com.example.flashcard.method;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;

public class UpdateCardActivity extends AppCompatActivity {

    public static final String EXTRA_CARDID = "cardId";
    public static int CARDID = 0;

    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            case R.id.action_more_vert:
                return true;
            case R.id.action_create_card:
                EditText editFrontText = (EditText) findViewById(R.id.change_front_text);
                String frontText = editFrontText.getText().toString();
                EditText editBackText = (EditText) findViewById(R.id.change_back_text);
                String backText = editBackText.getText().toString();
                if (!frontText.isEmpty() && !backText.isEmpty()) {
                    try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
                         SQLiteDatabase db = flashCardSQLiteHelper.getWritableDatabase()) {
                        FlashCardSQLiteHelper.updateCard(db, CARDID, frontText, backText);
                        Intent intent = new Intent(this, ListCardActivity.class);
                        intent.putExtra(ListCardActivity.EXTRA_CARDSETID, String.valueOf(CARDSETID));
                        startActivity(intent);
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
        try(FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(this);
            SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();
            Cursor cardCursor = db.query("CARD", new String[] {"FRONTTEXT","BACKTEXT"},
                    "_id=?", new String[] {Integer.toString((int)CARDID)}, null, null, null);) {
            if (cardCursor.moveToFirst()) {
                EditText frontText = (EditText) findViewById(R.id.change_front_text);
                EditText backText = (EditText) findViewById(R.id.change_back_text);
                frontText.setText(cardCursor.getString(0));
                backText.setText(cardCursor.getString(1));
            }

        } catch (SQLException e) {
            Toast.makeText(this,R.string.data_empty, Toast.LENGTH_SHORT);
        }
    }
}