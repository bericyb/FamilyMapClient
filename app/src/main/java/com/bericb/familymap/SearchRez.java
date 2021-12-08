package com.bericb.familymap;

import android.util.EventLogTags;

import java.util.List;

import model.Event;
import model.Person;

public class SearchRez {

    public List<Event> getMatchedEvents() {
        return matchedEvents;
    }

    public void setMatchedEvents(List<Event> matchedEvents) {
        this.matchedEvents = matchedEvents;
    }

    public List<Person> getMatchedPeople() {
        return matchedPeople;
    }

    public void setMatchedPeople(List<Person> matchedPeople) {
        this.matchedPeople = matchedPeople;
    }

    private List<Event> matchedEvents;
    private List<Person> matchedPeople;


    public SearchRez(List<Event> matchedEvents, List<Person> matchedPeople) {
        this.matchedEvents = matchedEvents;
        this.matchedPeople = matchedPeople;
    }
}
