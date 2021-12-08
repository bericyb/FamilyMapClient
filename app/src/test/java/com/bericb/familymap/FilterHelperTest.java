package com.bericb.familymap;

import static com.bericb.familymap.FilterHelper.drawSpouseLines;
import static com.bericb.familymap.FilterHelper.genderFilter;
import static com.bericb.familymap.FilterHelper.getFamilySideEvents;
import static com.bericb.familymap.FilterHelper.search;
import static org.junit.jupiter.api.Assertions.*;

import com.google.android.gms.maps.model.LatLng;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.provider.ContactsContract;
import android.telephony.TelephonyCallback;

import org.junit.Before;
import org.junit.Test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.Event;
import model.Person;
import requestResult.AllEventRequest;
import requestResult.AllEventResult;
import requestResult.AllPersonRequest;
import requestResult.AllPersonResult;
import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class FilterHelperTest {

    private ServerProxy serverProxy;
    private LoginResult loginResult;
    private Person person;

    @Before
    public void setUp() {
        serverProxy = new ServerProxy();

        RegisterRequest request = new RegisterRequest("spongebob", "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);

        RegisterResult result = serverProxy.register(request, "localhost", "8080");

        LoginRequest loginRequest = new LoginRequest("spongebob", "squidward");
        loginResult = serverProxy.login(loginRequest, "localhost", "8080");

        assertTrue(loginResult.isSuccess());

        AllEventResult eventRes = serverProxy.getEvents(new AllEventRequest("spongebob"), loginResult.getAuthToken());
        Map<String, Event> eventMap = new HashMap<String, Event>();
        for (Event event: eventRes.getData()) {
            eventMap.put(event.getEventID(), event);
        }
        DataCache.getInstance().setEvents(eventMap);
        DataCache.getInstance().setCopyEvents(eventMap);

        AllPersonResult res = serverProxy.getPeople(new AllPersonRequest("spongebob"), loginResult.getAuthToken());
        Map<String, Person> peopleMap = new HashMap<String, Person>();
        for (Person person: res.getData()) {
            peopleMap.put(person.getPersonID(), person);
        }
        DataCache.getInstance().setPeople(peopleMap);

    }

    @Test
    public void noSpouse() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());
        List<LatLng> result = drawSpouseLines(person);
        assertEquals(0, result.size());
    }

    @Test
    public void yesSpouse() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());
        String mother = person.getMotherID();
        Person father = DataCache.getInstance().getPersonByID(person.getFatherID());
        List<Event> momEvents = DataCache.getInstance().getPersonEvents(mother);
        momEvents.sort(Comparator.comparing(Event::getYear));
        Event motherBirth = momEvents.get(0);

        List<LatLng> result = drawSpouseLines(father);
        assertEquals(motherBirth.getLatitude(),result.get(0).latitude);
        assertEquals(motherBirth.getLongitude(), result.get(0).longitude);
    }

    @Test
    //This Test also tests for child relationships through parental relation.
    public void testFamilyDad() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        Map<String, Event> dad = getFamilySideEvents(person.getFatherID(), null);
        List<Event> dadEvents = DataCache.getInstance().getPersonEvents(person.getFatherID());

        Event dadBirth = dadEvents.get(0);
        assertNotNull(dad.get(dadBirth.getEventID()));
    }

    @Test
    //This Test also tests for child relationships through parental relation.
    public void testFamilyMom() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        Map<String, Event> mom = getFamilySideEvents(person.getMotherID(), null);
        List<Event> momEvents = DataCache.getInstance().getPersonEvents(person.getMotherID());

        Event dadBirth = momEvents.get(0);
        assertNotNull(mom.get(dadBirth.getEventID()));
    }

    @Test
    public void testMaleOnly() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        genderFilter(DataCache.getInstance().getCopyEvents(), "m");

        Map<String, Event> maleOnly = DataCache.getInstance().getEvents();
        assertEquals(0, DataCache.getInstance().getPersonEvents(person.getMotherID()).size());
    }

    @Test
    public void testFemaleOnly() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        genderFilter(DataCache.getInstance().getCopyEvents(), "f");
        Map<String, Event> femaleOnly = DataCache.getInstance().getEvents();
        assertEquals(0, DataCache.getInstance().getPersonEvents(person.getFatherID()).size());
    }

    @Test
    public void genderFail () {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        genderFilter(DataCache.getInstance().getCopyEvents(), "apacheHelicopter");
        Map<String, Event> apacheHelicopterOnly = DataCache.getInstance().getEvents();
        assertEquals(0, DataCache.getInstance().getEvents().size());
        DataCache.getInstance().setEvents(DataCache.getInstance().getCopyEvents());
    }

    @Test
    public void chronoLifeTest() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        List<Event> momEvents = DataCache.getInstance().getPersonEvents(person.getMotherID());
        assertTrue(momEvents.get(0).getYear() < momEvents.get(momEvents.size()-1).getYear());

        List<Event> dadEvents = DataCache.getInstance().getPersonEvents(person.getFatherID());
        assertTrue(dadEvents.get(0).getYear() < dadEvents.get(dadEvents.size()-1).getYear());
    }

    @Test
    public void searchPositive() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        Person mom = DataCache.getInstance().getPersonByID(person.getMotherID());
        String momName = mom.getFirstName();

        List<Event> momEvent = DataCache.getInstance().getPersonEvents(person.getMotherID());

        SearchRez res = search(momName);
        List<Person> searchPeople = res.getMatchedPeople();
        assertTrue(searchPeople.contains(mom));

        res = search(momEvent.get(0).getCity());

        List<Event> searchEvent = res.getMatchedEvents();
        assertTrue(searchEvent.contains(momEvent.get(0)));
    }

    @Test
    public void searchNegative() {
        person = DataCache.getInstance().getPersonByID(loginResult.getPersonID());

        SearchRez res = search(UUID.randomUUID().toString());

        assertEquals(0, res.getMatchedPeople().size());
        assertEquals(0, res.getMatchedEvents().size());
    }
}