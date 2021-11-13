package com.bericb.familymap;


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




//    Cached Data
//      Settings
    Settings settings;


//    Models
    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;

//    Cached data access methods
    Person getPersonByID(String id) { return null; }
    Event getEventByID(String id) { return null; }
    List<Event> getPersonEvents(String id) {return null;}




}
