package com.example.flashcard.drawer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.flashcard.MainActivity;
import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.method.CreateCardActivity;
import com.example.flashcard.method.ListCardActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupListView();
    }
    private void setupListView() {

        ListView listView = findViewById(R.id.setting_list_view);
        String[] setting_items = {
                getResources().getString(R.string.language),
                getResources().getString(R.string.notification)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, setting_items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            if (position == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.popup_language, null);
                builder.setView(view);
                builder.setTitle("test");
                AlertDialog dialog = builder.create();
                dialog.show();
                RadioGroup radioGroup = view.findViewById(R.id.radio_language);
                Locale defaultLocale = Locale.getDefault();
                String defaultLanguage = defaultLocale.getLanguage();
                radioGroup.check(getResources()
                        .getIdentifier(defaultLanguage, "id", getPackageName()));
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    String landcode = getResources().getResourceEntryName(checkedId);
                    setLocal(landcode);
                });
            }
        });
    }

    private void setLocal(String landcode) {
        Locale locale = new Locale(landcode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
    }

}
