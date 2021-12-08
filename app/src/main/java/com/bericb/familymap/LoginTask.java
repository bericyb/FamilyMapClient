package com.bericb.familymap;

import android.app.Person;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.PersonRequest;
import requestResult.PersonResult;

public class LoginTask implements Runnable{

    private static final String LOGIN_RESULT_STRING = "LoginResult";
    private static final String LOGIN_SUCCESS = "success";
    private final Handler messageHandler;
    private LoginRequest request;
    private String serverHost;
    private String serverPort;

    public LoginTask(Handler messageHandler, LoginRequest request, String serverHost, String serverPort) {
        this.request = request;
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public String login() {
        ServerProxy myProxy = new ServerProxy();

        LoginResult res = myProxy.login(request, serverHost, serverPort);

        if(!res.getSuccess()) {
            return("Username and/or Password incorrect");
        } else {
            DataGrabTask dataGrabber = new DataGrabTask(messageHandler, res);
            return(dataGrabber.grabData());
        }

    }

    @Override
    public void run() {
        ServerProxy myProxy = new ServerProxy();

        LoginResult res = myProxy.login(request, serverHost, serverPort);

        if(!res.getSuccess()) {
            sendMessage("Username and/or Password incorrect", "false");
        } else {
            Settings.getInstance().setUsername(res.getUsername());
            Settings.getInstance().setAuthToken(res.getAuthToken());
            Settings.getInstance().setPersonID(res.getPersonID());
            DataCache.getInstance().setSettings(Settings.getInstance());
            DataGrabTask dataGrabber = new DataGrabTask(messageHandler, res);
            sendMessage(dataGrabber.grabData(), "true");
        }

    }

    private void sendMessage(String result, String success) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(LOGIN_RESULT_STRING, result);
        messageBundle.putString(LOGIN_SUCCESS, success);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
