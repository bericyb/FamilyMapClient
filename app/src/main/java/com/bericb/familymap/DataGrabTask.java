package com.bericb.familymap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import model.Person;
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
        AllPersonRequest req = new AllPersonRequest(loginRes.getUsername());
        AllPersonResult res = myProxy.getPeople(req, loginRes.getAuthToken());
        Map<String, Person> peopleMap = new HashMap<String, Person>();
        for (Person person: res.getData()) {
            peopleMap.put(person.getPersonID(), person);
        }
        DataCache.getInstance().setPeople(peopleMap);
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
