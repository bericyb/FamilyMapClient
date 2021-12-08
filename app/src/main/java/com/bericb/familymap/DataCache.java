package com.bericb.familymap;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class DataCache {

    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }


    private DataCache() {
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }

    //    Cached Data
//      Settings
    private Settings settings;


//    Models
    private Map<String, Person> people;
    private Map<String, Event> events;

    public Map<String, Event> getCopyEvents() {
        return copyEvents;
    }

    public void setCopyEvents(Map<String, Event> copyEvents) {
        this.copyEvents = copyEvents;
    }

    private Map<String, Event> copyEvents;
    private Map<String, List<Event>> personEvents;

//    Cached data access methods
    Person getPersonByID(String id) {
        return people.get(id);
    }
    Event getEventByID(String id) { return events.get(id); }

    @RequiresApi(api = Build.VERSION_CODES.N)
    List<Event> getPersonEvents(String id) {
        List<Event> personEvents = new ArrayList<Event>();

        for (Object entry :
                events.values()) {
            Event event = (Event) entry;
            if (event.getPersonID().equals(id)) {
                personEvents.add(event);
            }
        }
        personEvents.sort(Comparator.comparing(Event::getYear));

        return personEvents;
    }


    public void clear() {
        instance = null;
        instance = new DataCache();
    }


}
