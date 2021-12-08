package com.bericb.familymap;

import static android.graphics.Color.BLUE;

import static com.bericb.familymap.FilterHelper.getFamilySideEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class SettingsActivity extends AppCompatActivity {

    private Switch lifeStory;
    private Switch familyTree;
    private Switch spouseSwitch;
    private Switch fatherSwitch;
    private Switch motherSwitch;
    private Switch maleSwitch;
    private Switch femaleSwitch;
    private TextView logout;
    private Map<String, Event> rootEvents = new HashMap<String, Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle(R.string.settingsActivity);

        lifeStory = findViewById(R.id.lifeStorySwitch);
        familyTree = findViewById(R.id.familyTreeSwitch);
        spouseSwitch = findViewById(R.id.spouseSwitch);
        fatherSwitch = findViewById(R.id.fatherSwitch);
        motherSwitch = findViewById(R.id.motherSwitch);
        maleSwitch = findViewById(R.id.maleSwitch);
        femaleSwitch = findViewById(R.id.femaleSwitch);
        logout = findViewById(R.id.logout);

        DataCache data = DataCache.getInstance();

        Settings settings = data.getSettings();

        lifeStory.setChecked(settings.isLifeStory());
        familyTree.setChecked(settings.isFamilyTree());
        spouseSwitch.setChecked(settings.isSpouseLines());
        fatherSwitch.setChecked(settings.isFatherSide());
        motherSwitch.setChecked(settings.isMotherSide());
        maleSwitch.setChecked(settings.isMaleEvent());
        femaleSwitch.setChecked(settings.isFemaleEvent());

        lifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeStory(isChecked);
            }
        });

        familyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTree(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLines(isChecked);
            }
        });

        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Only Mother's side
                if (!isChecked && settings.isMotherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    rootEvents = new HashMap<String, Event>();

                    rootEvents = getFamilySideEvents(null, rootUser.getMotherID());
                    String spouseID = rootUser.getSpouseID();

                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || eventPerson.equals(rootUser.getPersonID())) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }


                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                //No sides just self and spouse
                if (!isChecked && !settings.isMotherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    String spouseID = rootUser.getSpouseID();
                    rootEvents = new HashMap<String, Event>();
                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || settings.getPersonID().equals(eventPerson)) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }
                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                //Both sides
                if (isChecked && settings.isMotherSide()) {
                    rootEvents = DataCache.getInstance().getCopyEvents();
                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                if(isChecked && !settings.isMotherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    rootEvents = new HashMap<String, Event>();
                    rootEvents = getFamilySideEvents(rootUser.getFatherID(), null);
                    String spouseID = rootUser.getSpouseID();

                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || settings.getPersonID().equals(eventPerson)) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }


                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }
                settings.setFatherSide(isChecked);
            }
        });

        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked && settings.isFatherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    rootEvents = new HashMap<String, Event>();
                    rootEvents = getFamilySideEvents(rootUser.getFatherID(), null);
                    String spouseID = rootUser.getSpouseID();

                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || rootUser.getPersonID().equals(eventPerson)) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }


                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                //No sides just self and spouse
                if (!isChecked && !settings.isFatherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    String spouseID = rootUser.getSpouseID();
                    rootEvents = new HashMap<String, Event>();
                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || settings.getPersonID().equals(eventPerson)) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }
                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                //Both sides
                if (isChecked && settings.isFatherSide()) {
                    rootEvents = DataCache.getInstance().getCopyEvents();
                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(DataCache.getInstance().getCopyEvents());
                    }
                }

                if(isChecked && !settings.isMotherSide()) {
                    Person rootUser = DataCache.getInstance().getPersonByID(settings.getPersonID());
                    rootEvents = new HashMap<String, Event>();
                    rootEvents = getFamilySideEvents(rootUser.getFatherID(), null);
                    String spouseID = rootUser.getSpouseID();

                    //Don't forget the spouse!
                    Map<String, Event> events = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            events.values()) {
                        Event event = (Event) entry;
                        String eventPerson = event.getPersonID();
                        String matchID = spouseID;
                        if (eventPerson.equals(matchID) || rootUser.getPersonID().equals(eventPerson)) {
                            rootEvents.put(event.getEventID(), event);
                        }
                    }


                    if (settings.isFemaleEvent() && !settings.isMaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "f");
                    }
                    else if (settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        FilterHelper.genderFilter(rootEvents, "m");
                    }
                    else if (!settings.isMaleEvent() && !settings.isFemaleEvent()) {
                        DataCache.getInstance().setEvents(new HashMap<String, Event>());
                    } else {
                        DataCache.getInstance().setEvents(rootEvents);
                    }
                }

                settings.setMotherSide(isChecked);
            }
        });

        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !settings.isFemaleEvent()) {
                    DataCache.getInstance().setEvents(new HashMap<String, Event>());
                }
                if (!isChecked && settings.isFemaleEvent()) {
                    Map<String, Event> femaleOnly = new HashMap<String, Event>();

                    Map<String, Event> master = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            master.values()) {
                        Event event = (Event) entry;
                        if (DataCache.getInstance().getPersonByID(event.getPersonID()).getGender().equals("f")) {
                            femaleOnly.put(event.getEventID(), event);
                        }
                    }
                    DataCache.getInstance().setEvents(femaleOnly);
                }
                if (isChecked && settings.isFemaleEvent()) {
                    DataCache.getInstance().setEvents(DataCache.getInstance().getCopyEvents());
                }
                if (isChecked && !settings.isFemaleEvent()) {
                    Map<String, Event> femaleOnly = new HashMap<String, Event>();

                    Map<String, Event> master = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            master.values()) {
                        Event event = (Event) entry;
                        if (DataCache.getInstance().getPersonByID(event.getPersonID()).getGender().equals("m")) {
                            femaleOnly.put(event.getEventID(), event);
                        }
                    }
                    DataCache.getInstance().setEvents(femaleOnly);
                }
                settings.setMaleEvent(isChecked);
            }
        });

        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !settings.isMaleEvent()) {
                    DataCache.getInstance().setEvents(new HashMap<String, Event>());
                }
                if (!isChecked && settings.isMaleEvent()) {
                    Map<String, Event> maleOnly = new HashMap<String, Event>();

                    Map<String, Event> master = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            master.values()) {
                        Event event = (Event) entry;
                        if (DataCache.getInstance().getPersonByID(event.getPersonID()).getGender().equals("m")) {
                            maleOnly.put(event.getEventID(), event);
                        }
                    }
                    DataCache.getInstance().setEvents(maleOnly);
                }
                if (isChecked && settings.isMaleEvent()) {
                    DataCache.getInstance().setEvents(DataCache.getInstance().getCopyEvents());
                }
                if (isChecked && !settings.isMaleEvent()){
                    Map<String, Event> maleOnly = new HashMap<String, Event>();

                    Map<String, Event> master = DataCache.getInstance().getCopyEvents();
                    for (Object entry :
                            master.values()) {
                        Event event = (Event) entry;
                        if (DataCache.getInstance().getPersonByID(event.getPersonID()).getGender().equals("f")) {
                            maleOnly.put(event.getEventID(), event);
                        }
                    }
                    DataCache.getInstance().setEvents(maleOnly);
                }

                settings.setFemaleEvent(isChecked);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().clear();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        });

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