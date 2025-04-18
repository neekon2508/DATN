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
import com.example.flashcard.data.LocaleHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.method.CreateCardActivity;
import com.example.flashcard.method.ListCardActivity;
import com.example.flashcard.setting.AppearanceActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
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
                getResources().getString(R.string.notification),
                getResources().getString(R.string.appearance),
                getResources().getString(R.string.about)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, setting_items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            switch (position) {
                case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.popup_language, null);
                builder.setView(view);
                builder.setTitle("test");
                AlertDialog dialog = builder.create();
                dialog.show();
                RadioGroup radioGroup = view.findViewById(R.id.radio_language);
                String defaultLanguage = LocaleHelper.language;
                radioGroup.check(getResources()
                        .getIdentifier(defaultLanguage, "id", getPackageName()));
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        String landcode = getResources().getResourceEntryName(checkedId);
                        LocaleHelper.setAppLocale(this, landcode);
                Intent intent = new Intent(this, SettingActivity.class);
                finish();
                startActivity(intent);
                });
                break;
                case 1:
                    break;
                case 2:
                    Intent intent = new Intent(this, AppearanceActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }



}
