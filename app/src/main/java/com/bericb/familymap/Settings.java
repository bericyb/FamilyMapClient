package com.bericb.familymap;

public class Settings {

    private static Settings instance = new Settings();
    public static Settings getInstance() {return instance;}
    private Settings() {}


    private String username;
    private String password;
    private String authToken;
    private String personID;
    private boolean lifeStory = true;
    private boolean familyTree = true;
    private boolean spouseLines = true;
    private boolean fatherSide = true;
    private boolean motherSide = true;
    private boolean maleEvent = true;
    private boolean femaleEvent = true;

    public boolean isLifeStory() {
        return lifeStory;
    }

    public void setLifeStory(boolean lifeStory) {
        this.lifeStory = lifeStory;
    }

    public boolean isFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(boolean familyTree) {
        this.familyTree = familyTree;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvent() {
        return maleEvent;
    }

    public void setMaleEvent(boolean maleEvent) {
        this.maleEvent = maleEvent;
    }

    public boolean isFemaleEvent() {
        return femaleEvent;
    }

    public void setFemaleEvent(boolean femaleEvent) {
        this.femaleEvent = femaleEvent;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }


    public static void setInstance(Settings instance) {
        Settings.instance = instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
