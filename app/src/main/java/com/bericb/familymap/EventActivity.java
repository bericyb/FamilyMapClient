package com.bericb.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import model.Event;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentByTag("MAP_FRAG");

        Bundle extras = getIntent().getExtras();

        String eventID = extras.getString("EVENT");
        Event currEvent = DataCache.getInstance().getEventByID(eventID);

        MapFragment mapFrag = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("EVENT", eventID);
        mapFrag.setArguments(bundle);


        fragmentManager.beginTransaction().add(R.id.fragment_container, mapFrag).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}