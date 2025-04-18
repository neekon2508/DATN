package com.example.flashcard.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.flashcard.R;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.LocaleHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.drawer.SettingActivity;

public class AppearanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupListView();
    }

    private void setupListView() {

        ListView listView = findViewById(R.id.appearance_list_view);
        String[] setting_items = {
                getResources().getString(R.string.theme),
                getResources().getString(R.string.background),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, setting_items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            switch (position) {
                case 0:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.popup_theme, null);
                    builder.setView(view);
                    builder.setTitle("test");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    String themeName = ThemeManager.getTheme(this);
                    RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_theme);
                    radioGroup.check(getResources()
                            .getIdentifier(themeName, "id", getPackageName()));
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        String newThemeName = getResources().getResourceEntryName(checkedId);
                        ThemeManager.updateTheme(this, newThemeName);
                        Intent intent = new Intent(this, AppearanceActivity.class);
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