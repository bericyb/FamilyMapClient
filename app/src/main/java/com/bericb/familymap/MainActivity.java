package com.bericb.familymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private MenuItem search;
    private MenuItem settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.fragment_container);
        if(currentFrag == null) {
            recreate();
            LoginFragment loginFrag = new LoginFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, loginFrag).commit();
        }
        else {
            MapFragment mapFrag = new MapFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapFrag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        search = menu.findItem(R.id.action_search);
        settings = menu.findItem(R.id.action_settings);

        if (DataCache.getInstance().getSettings() != null) {
            search.setVisible(true);
            settings.setVisible(true);
        } else {
            search.setVisible(false);
            settings.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        search = menu.findItem(R.id.action_search);
        settings = menu.findItem(R.id.action_settings);

        if (DataCache.getInstance() != null) {
            search.setVisible(true);
            settings.setVisible(true);
        } else {
            search.setVisible(false);
            settings.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return true;
    }
}