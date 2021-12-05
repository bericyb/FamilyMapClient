package com.bericb.familymap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.Person;
import requestResult.AllEventRequest;
import requestResult.AllEventResult;
import requestResult.AllPersonRequest;
import requestResult.AllPersonResult;
import requestResult.LoginResult;

public class DataGrabTask implements Runnable {

    private static final String DATA_GRAB_STRING = "DataResult";
    private final Handler messageHandler;
    private LoginResult loginRes;

    public DataGrabTask(Handler messageHandler, LoginResult res) {
        this.messageHandler = messageHandler;
        this.loginRes = res;
    }

    public String grabData() {
        ServerProxy myProxy = new ServerProxy();

        //Grabbing People data
        AllPersonRequest req = new AllPersonRequest(loginRes.getUsername());
        AllPersonResult res = myProxy.getPeople(req, loginRes.getAuthToken());
        Map<String, Person> peopleMap = new HashMap<String, Person>();
        for (Person person: res.getData()) {
            peopleMap.put(person.getPersonID(), person);
        }
        DataCache.getInstance().setPeople(peopleMap);

        //Grabbing Event data
        AllEventRequest eventRequest =  new AllEventRequest(loginRes.getUsername());
        AllEventResult eventRes = myProxy.getEvents(eventRequest, loginRes.getAuthToken());
        Map<String, Event> eventMap = new HashMap<String, Event>();
        for (Event event: eventRes.getData()) {
            eventMap.put(event.getEventID(), event);
        }
        DataCache.getInstance().setEvents(eventMap);

        //TODO: Grab and set person events


        //Get user and return user's name in welcome string.
        Person userPerson = DataCache.getInstance().getPersonByID(loginRes.getPersonID());
        return("Welcome " + userPerson.getFirstName() + " " + userPerson.getLastName());
    }


    @Override
    public void run() {
        ServerProxy myProxy = new ServerProxy();
        AllPersonRequest req = new AllPersonRequest(loginRes.getUsername());
        AllPersonResult res = myProxy.getPeople(req, loginRes.getAuthToken());
        Map<String, Person> peopleMap = new HashMap<String, Person>();
        for (Person person: res.getData()) {
            peopleMap.put(person.getPersonID(), person);
        }
        DataCache.getInstance().setPeople(peopleMap);
        Person userPerson = DataCache.getInstance().getPersonByID(loginRes.getPersonID());
        sendMessage("Welcome " + userPerson.getFirstName() + " " + userPerson.getLastName());
    }


    public void sendMessage(String result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(DATA_GRAB_STRING, result);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
