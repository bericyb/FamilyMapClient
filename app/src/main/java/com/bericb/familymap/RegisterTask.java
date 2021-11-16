package com.bericb.familymap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import requestResult.LoginRequest;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class RegisterTask implements Runnable {

    private static final String REGISTER_RESULT_STRING = "RegisterResult";
    private final Handler messageHandler;
    private RegisterRequest req;
    private String serverHost;
    private String serverPort;

    public RegisterTask(Handler uiMessageHandler, RegisterRequest req, String serverHost, String serverPort) {
        this.messageHandler = uiMessageHandler;
        this.req = req;
        this.serverHost = serverHost;
        this.serverPort = serverPort;

    }

    @Override
    public void run() {
        ServerProxy myProxy = new ServerProxy();

        RegisterResult res = myProxy.register(req, serverHost, serverPort);

        if(!res.getSuccess()) {
            sendMessage(res.getMessage());
        } else {
            LoginRequest req = new LoginRequest(res.getUsername(), this.req.getPassword());
            LoginTask loginTask = new LoginTask(messageHandler, req, serverHost, serverPort);
            sendMessage(loginTask.login());
        }
    }

    private void sendMessage(String result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(REGISTER_RESULT_STRING, result);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
