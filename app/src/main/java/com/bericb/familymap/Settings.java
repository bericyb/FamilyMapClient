package com.bericb.familymap;

public class Settings {

    private static Settings instance = new Settings();
    public static Settings getInstance() {return instance;}
    private Settings() {}


    private String username;
    private String password;
    private String authToken;
    private String personID;



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
