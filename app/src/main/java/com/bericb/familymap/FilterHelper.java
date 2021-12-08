package com.bericb.familymap;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class FilterHelper {

    private static List<LatLngData> familyTree;
    private static List<Event> matchedEvents;
    private static List<Person> matchedPeople;
    private static Map<String, Event> rootEvents;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<LatLng> drawSpouseLines(Person person) {
        List<LatLng> res = new ArrayList<LatLng>();
        if (person.getSpouseID() != null) {
            String spouseID = person.getSpouseID();
            List<Event> spouseEvents = DataCache.getInstance().getPersonEvents(spouseID);
            for (Object entry :
                    spouseEvents) {
                Event event = (Event) entry;
                if (event.getEventType().equals("birth")) {

                    res.add(new LatLng(event.getLatitude(), event.getLongitude()));
                }
            }
        }
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<LatLngData> familyTreeHelper(String fatherID, String motherID, LatLng currLoc) {
        familyTree = new ArrayList<LatLngData>();
        drawFamilyTree(fatherID, motherID, currLoc, 0);
        return familyTree;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void drawFamilyTree(String fatherID, String motherID, LatLng currLoc, int level) {
        if (fatherID != null) {
            List<Event> familyEvents = new ArrayList<Event>();
            Map<String, Event> events = DataCache.getInstance().getEvents();
            for (Object entry :
                    events.values()) {
                Event event = (Event) entry;
                String eventPerson = event.getPersonID();
                if (eventPerson.equals(fatherID)) {
                    familyEvents.add(event);
                }
            }

            familyEvents.sort(Comparator.comparing(Event::getYear));
            if (familyEvents.size() > 0) {
                Event fatherBirth = familyEvents.get(0);
                familyTree.add(new LatLngData(currLoc, new LatLng(fatherBirth.getLatitude(), fatherBirth.getLongitude()), level));
                Person parent = DataCache.getInstance().getPersonByID(fatherID);
                drawFamilyTree(parent.getFatherID(), parent.getMotherID(), new LatLng(fatherBirth.getLatitude(), fatherBirth.getLongitude()), level + 3);
            }
        }
        if (motherID != null) {
            List<Event> familyEvents = new ArrayList<Event>();
            Map<String, Event> events = DataCache.getInstance().getEvents();
            for (Object entry :
                    events.values()) {
                Event event = (Event) entry;
                String eventPerson = event.getPersonID();
                if (eventPerson.equals(motherID)) {
                    familyEvents.add(event);
                }
            }

            familyEvents.sort(Comparator.comparing(Event::getYear));
            if (familyEvents.size() > 0) {
                Event fatherBirth = familyEvents.get(0);
                familyTree.add(new LatLngData(currLoc, new LatLng(fatherBirth.getLatitude(), fatherBirth.getLongitude()), level));
                Person parent = DataCache.getInstance().getPersonByID(motherID);
                drawFamilyTree(parent.getFatherID(), parent.getMotherID(), new LatLng(fatherBirth.getLatitude(), fatherBirth.getLongitude()), level + 3);
            }
        }
    }

    public static List<LatLng> drawLifeStory(String personID) {
        List<LatLng> res = new ArrayList<LatLng>();
        Map<String, Event> events = DataCache.getInstance().getEvents();
        for (Object entry :
                events.values()) {
            Event event = (Event) entry;
            String eventPerson = event.getPersonID();
            if (eventPerson.equals(personID)) {
                res.add(new LatLng(event.getLatitude(), event.getLongitude()));
            }
        }
        return res;
    }

    public static void genderFilter(Map<String, Event> events, String gender) {
        Map<String, Event> genderOnly = new HashMap<String, Event>();

        Map<String, Event> master = events;
        for (Object entry :
                master.values()) {
            Event event = (Event) entry;
            if (DataCache.getInstance().getPersonByID(event.getPersonID()).getGender().equals(gender)) {
                genderOnly.put(event.getEventID(), event);
            }
        }
        DataCache.getInstance().setEvents(genderOnly);
    }

    public static Map<String, Event> getFamilySideEvents(String fatherID, String MotherID) {
        rootEvents = new HashMap<String , Event>();
        getFamilySideEventsHelper(fatherID, MotherID);
        return rootEvents;
    }

    private static void getFamilySideEventsHelper(String fatherID, String motherID) {
        Map<String, Event> result = new HashMap<String, Event>();

        if (fatherID != null) {
            Map<String, Event> events = DataCache.getInstance().getCopyEvents();
            for (Object entry :
                    events.values()) {
                Event event = (Event) entry;
                String eventPerson = event.getPersonID();
                String matchID = fatherID;
                if (eventPerson.equals(matchID)) {
                    rootEvents.put(event.getEventID(), event);
                }
            }

            Person parent = DataCache.getInstance().getPersonByID(fatherID);
            getFamilySideEventsHelper(parent.getFatherID(), parent.getMotherID());
        }


        if (motherID != null) {
            Map<String, Event> events = DataCache.getInstance().getCopyEvents();
            for (Object entry :
                    events.values()) {
                Event event = (Event) entry;
                String eventPerson = event.getPersonID();
                String matchID = motherID;
                if (eventPerson.equals(matchID)) {
                    rootEvents.put(event.getEventID(), event);
                }
            }
            Person mother = DataCache.getInstance().getPersonByID(motherID);
            getFamilySideEventsHelper(mother.getFatherID(), mother.getMotherID());
        }
    }

    public static SearchRez search(CharSequence s) {
        matchedEvents = new ArrayList<Event>();
        matchedPeople = new ArrayList<Person>();

        if (s == "") {
            return new SearchRez(matchedEvents, matchedPeople);
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
        return new SearchRez(matchedEvents, matchedPeople);
    }
}
