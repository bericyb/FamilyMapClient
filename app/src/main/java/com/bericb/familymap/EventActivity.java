package com.bericb.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import model.Event;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentByTag("MAP_FRAG");

        Bundle extras = getIntent().getExtras();

        String eventID = extras.getString(String.valueOf(R.string.eventIntent));
        Event currEvent = DataCache.getInstance().getEventByID(eventID);

        EventMapsFragment mapFrag = new EventMapsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("EVENT", eventID);
        mapFrag.setArguments(bundle);


        fragmentManager.beginTransaction().add(R.id.fragment_container, mapFrag).commit();
    }
}