package com.bericb.familymap;

import static com.bericb.familymap.DataCache.getInstance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private ExpandableListView list;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstName = findViewById(R.id.personFirstName);
        lastName = findViewById(R.id.personLastName);
        gender = findViewById(R.id.personGender);

        Bundle extras = getIntent().getExtras();

        String personID = extras.getString("PERSON");

        Person currentPerson = DataCache.getInstance().getPersonByID(personID);

        DataCache data = DataCache.getInstance();

        List<Person> family = new ArrayList<Person>();
        if (currentPerson.getFatherID() != null) {
            family.add(data.getPersonByID(currentPerson.getFatherID()));
        }
        if (currentPerson.getMotherID() != null) {
            family.add(data.getPersonByID(currentPerson.getMotherID()));
        }
        if (currentPerson.getSpouseID() != null) {
            family.add(data.getPersonByID(currentPerson.getSpouseID()));
        }


        Map<String, Person> people = data.getPeople();
        for (Object entry :
                people.values()) {
            Person person = (Person) entry;
            if (Objects.equals(person.getFatherID(), currentPerson.getPersonID()) || Objects.equals(person.getMotherID(), currentPerson.getPersonID())) {
                family.add(person);
            }
        }

        List<Event> familyEvents = new ArrayList<Event>();
        Map<String, Event> events = data.getEvents();
        for (Object entry :
                events.values()) {
            Event event = (Event) entry;
            String eventPerson = event.getPersonID();
            String matchID = currentPerson.getPersonID();
            if (eventPerson.equals(matchID)) {
                familyEvents.add(event);
            }
        }

        familyEvents.sort(Comparator.comparing(Event::getYear));


        firstName.setText(currentPerson.getFirstName());
        lastName.setText(currentPerson.getLastName());
        switch (currentPerson.getGender()) {
            case "m":
                gender.setText(R.string.male);
                break;
            case "f":
                gender.setText(R.string.female);
                break;
        }

        list = findViewById(R.id.expandablePerson);
        list.setAdapter(new PersonActivity.ExpandableListAdapter(familyEvents, family, currentPerson) {
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activitiy_person, menu);

        return true;

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


    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT = 0;
        private static final int PERSON = 1;

        private final List<Event> events;
        private final List<Person> family;
        private final Person currentPerson;

        private final String father;
        private final String mother;
        private final String spouse;

        ExpandableListAdapter(List<Event> familyEvents, List<Person> family, Person currentPerson) {
            this.events = familyEvents;
            this.family = family;
            this.currentPerson = currentPerson;
            father = currentPerson.getFatherID();
            mother = currentPerson.getMotherID();
            spouse = currentPerson.getSpouseID();
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT:
                    return events.size();
                case PERSON:
                    return family.size();
                default:
                    throw new IllegalArgumentException("BAD CODE... BAD!");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch(groupPosition) {
                case EVENT:
                    return getString(R.string.lifeEvents);
                case PERSON:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("BAD Groups!");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT:
                    return events.get(childPosition);
                case PERSON:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("BAD Children!");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {return groupPosition;}

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch(groupPosition) {
                case EVENT:
                    titleView.setText(R.string.lifeEvents);
                    break;
                case PERSON:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Bad groups view");
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition) {
                case EVENT:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("BAD child views!");
            }
            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initializeEventView(View eventView, final int childPosition) {
            Event event = events.get(childPosition);
            TextView eventInfo = eventView.findViewById(R.id.event_info);
            TextView name = eventView.findViewById(R.id.personName);
            String info = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + "(" + event.getYear() + ")";
            eventInfo.setText(info);
            Person eventPerson = DataCache.getInstance().getPersonByID(event.getPersonID());
            String fullName = eventPerson.getFirstName() + " " + eventPerson.getLastName();
            name.setText(fullName);
            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(String.valueOf(R.string.eventIntent), event.getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View personView, final int childPosition) {
            Person person = family.get(childPosition);
            TextView name = personView.findViewById(R.id.personName);
            String fullName = person.getFirstName() + " " + person.getLastName();
            TextView relation = personView.findViewById(R.id.personRelation);

            String id = person.getPersonID();

            if(Objects.equals(id, father)) {
                relation.setText(R.string.father);
            } else if (Objects.equals(id, mother)) {
                relation.setText(R.string.mother);
            } else if (Objects.equals(id, spouse)) {
                relation.setText(R.string.spouse);
            } else {
                relation.setText(R.string.child);
            }

            switch (person.getGender()) {
                case "m":
                    name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male,0,0,0);
                    break;
                case "f":
                    name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.female,0,0,0);
                    break;
            }

            name.setText(fullName);
            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(String.valueOf("PERSON"), person.getPersonID());
                    startActivity(intent);
                }
            });
        }
    }
}
