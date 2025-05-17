package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.flashcard.data.BackgroundManager;
import com.example.flashcard.data.FlashCardSQLiteHelper;
import com.example.flashcard.data.LocaleHelper;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.drawer.FlashCardSetFragment;
import com.example.flashcard.drawer.HelpFragment;
import com.example.flashcard.drawer.LogInActivity;
import com.example.flashcard.drawer.SettingActivity;
import com.example.flashcard.drawer.StatisticFragment;
import com.example.flashcard.drawer.SupportFragment;
import com.example.flashcard.method.ListCardFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getSharedPreferences("USER", MODE_PRIVATE);
        LocaleHelper.setAppLocale(this);
        if (user.getString("username",null)==null)
            startActivity(new Intent(this,LogInActivity.class));
        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BackgroundManager.setDefaultBackground(this, navigationView);

        checkUser(navigationView);

        Fragment fragment = new FlashCardSetFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "CARD_SET_TAG");
        ft.commit();
    }

    private void checkUser(NavigationView navigationView) {
        String username = user.getString("username", null);
        if (username != null) {
            ((MenuItem)navigationView.getMenu().findItem(R.id.nav_log_in)).setVisible(false);
            ((MenuItem)navigationView.getMenu().findItem(R.id.nav_log_out)).setVisible(true);
            ((TextView)navigationView.getHeaderView(0).
                    findViewById(R.id.hello_user)).setText(getString(R.string.hello_user)+", "+username);
        }
        else {
            ((MenuItem)navigationView.getMenu().findItem(R.id.nav_log_in)).setVisible(true);
            ((MenuItem)navigationView.getMenu().findItem(R.id.nav_log_out)).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
            case R.id.nav_cardSet:
                fragment = new FlashCardSetFragment();
                break;
            case R.id.nav_statistic:
                fragment = new StatisticFragment();
                break;
            case R.id.nav_setting:
                intent = new Intent(this, SettingActivity.class);
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();
                break;
            case R.id.nav_support:
                fragment = new SupportFragment();
                break;
            case R.id.nav_log_in:
                intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_log_out:
                user.edit().clear().apply();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                fragment = new FlashCardSetFragment();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        else
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list_cardset, menu); // Nếu là ListCardFragment, dùng menu list_cardset
            MenuItem searchItem = menu.findItem(R.id.action_search_cardset);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    sendSearchQueryToFragment(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    sendSearchQueryToFragment(newText);
                    return true;
                }
            });
        return true;
    }
    private void sendSearchQueryToFragment(String query) {
        FlashCardSetFragment fragment = (FlashCardSetFragment) getSupportFragmentManager().findFragmentByTag("CARD_SET_TAG");
        if (fragment != null && fragment.isAdded()) {
            Log.d("DEBUG", "Gửi truy vấn tìm kiếm: " + query);
            fragment.filterList(query);
        } else {
            Log.e("ERROR", "Không tìm thấy Fragment!");
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}