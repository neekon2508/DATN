package com.example.flashcard.method;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.flashcard.MainActivity;
import com.example.flashcard.R;
import com.example.flashcard.data.ThemeManager;
import com.example.flashcard.drawer.FlashCardSetFragment;
import com.example.flashcard.drawer.HelpFragment;
import com.example.flashcard.drawer.LogInActivity;
import com.example.flashcard.drawer.SettingActivity;
import com.example.flashcard.drawer.StatisticFragment;
import com.example.flashcard.drawer.SupportFragment;
import com.example.flashcard.service.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Retrofit;


public class ListCardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String EXTRA_CARDSETID = "cardsetId";
    public static int CARDSETID = 0;
    private static Retrofit retrofit = RetrofitClient.getRetrofitInstance();
    SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getSharedPreferences("USER", MODE_PRIVATE);

        ThemeManager.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent intent = getIntent();
        CARDSETID = Integer.parseInt(intent.getStringExtra(EXTRA_CARDSETID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkUser(navigationView);

        Bundle bundle = new Bundle();
        bundle.putInt("cardsetId", CARDSETID);
        ListCardFragment fragment = new ListCardFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_card, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_card:
                Intent intent = new Intent(this, CreateCardActivity.class);
                intent.putExtra(CreateCardActivity.EXTRA_CARDSETID, String.valueOf(CARDSETID));
                startActivity(intent);
                return true;
            case R.id.action_search_card:

                return true;
            case R.id.action_more_vert:

                return true;
            default:
                return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

}