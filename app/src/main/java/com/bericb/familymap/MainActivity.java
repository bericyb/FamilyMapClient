package com.bericb.familymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

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
}