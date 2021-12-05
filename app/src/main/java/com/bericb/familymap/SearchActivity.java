package com.bericb.familymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENT_ITEM = 0;
    private static final int PERSON_ITEM = 1;
    private EditText searchField;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    private List<Person> matchedPeople = new java.util.ArrayList<Person>();
    private List<Event> matchedEvents = new ArrayList<Event>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = findViewById(R.id.search_field);

        search("");
        recyclerView = findViewById(R.id.search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        adapter = new SearchAdapter( matchedEvents, matchedPeople);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//          TODO: implement search functionality.


            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s);
                adapter.updateData(matchedEvents, matchedPeople);
                adapter.notifyDataSetChanged();
            }

        });
    }


    private void search(CharSequence s) {

        if (s == "") {
            return;
        }
        s = s.toString().toLowerCase();
        int sNum;
        try {
            sNum = Integer.parseInt(s.toString());

        } catch (Exception e){
            sNum = 999999;
        }

        Map<String, Event> events = DataCache.getInstance().getEvents();
        Map<String, Person> people = DataCache.getInstance().getPeople();

        matchedEvents = new ArrayList<Event>();
        matchedPeople = new ArrayList<Person>();

        for (Object entry :
                events.values()) {
            Event event = (Event) entry;
            if (event.getCity().toLowerCase().contains(s) || event.getCountry().toLowerCase().contains(s) || event.getEventType().contains(s) || event.getYear() == sNum) {
                matchedEvents.add(event);
            }
        }

        for(Object entry:
                people.values()) {
            Person person = (Person) entry;

            if (person.getFirstName().toLowerCase().contains(s) || person.getLastName().toLowerCase().contains(s)) {
                matchedPeople.add(person);
            }
        }
        System.out.println(matchedEvents);

        System.out.println(matchedPeople);



    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        private List<Event> events;
        private List<Person> people;

        SearchAdapter(List<Event> events, List<Person> people) {
            this.events = events;
            this.people = people;
        }
        public void updateData(List<Event> events, List<Person> people) {
            this.events = events;
            this.people = people;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == EVENT_ITEM) {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM : EVENT_ITEM ;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.size() + people.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Person person;
        private Event event;

        private TextView name;
        private TextView eventInfo;

        private final int viewType;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_ITEM) {
                //TODO: GET and SET event item fields and such.
                name = itemView.findViewById(R.id.personName);
                eventInfo = null;
            } else {
                //TODO: GET and SET person item fields and such.
                name = itemView.findViewById(R.id.personName);
                eventInfo = itemView.findViewById(R.id.event_info);
            }

        }

        @Override
        public void onClick(View v) {
            //TODO: Open up new activities
            if (viewType == PERSON_ITEM) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("PERSON", person.getPersonID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("EVENT", event.getEventID());
                startActivity(intent);
            }
        }

        private void bind(Event event) {
            this.event = event;
            String info = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + "(" + event.getYear() + ")";
            eventInfo.setText(info);
            Person eventPerson = DataCache.getInstance().getPersonByID(event.getPersonID());
            String fullName = eventPerson.getFirstName() + " " + eventPerson.getLastName();
            name.setText(fullName);
        }

        private void bind(Person person) {
            this.person = person;

            String gender = person.getGender();
            switch (gender) {
                case "m" : name.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.male, 0,0,0);
                    break;
                case "f" :
                    name.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.female, 0, 0, 0);
                    break;
            }


            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);


        }

    }
}