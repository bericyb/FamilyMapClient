package com.bericb.familymap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import requestResult.AllEventRequest;
import requestResult.AllEventResult;
import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class FetchEventsTest {

    private ServerProxy serverProxy;
    private LoginResult loginResult;

    @Before
    public void setUp() {
        serverProxy = new ServerProxy();

        RegisterRequest request = new RegisterRequest("spongebob", "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);

        RegisterResult result = serverProxy.register(request, "localhost", "8080");

        LoginRequest loginRequest = new LoginRequest("spongebob", "squidward");
        loginResult = serverProxy.login(loginRequest, "localhost", "8080");
    }

    @Test
    public void fetchFail() {
        AllEventRequest request = new AllEventRequest(UUID.randomUUID().toString());
        AllEventResult result = serverProxy.getEvents(request, UUID.randomUUID().toString());

        assertFalse(result.getSuccess());
    }

    @Test
    public void fetchSuccess() {
        AllEventRequest request = new AllEventRequest(loginResult.getUsername());
        AllEventResult result = serverProxy.getEvents(request, loginResult.getAuthToken());

        assertTrue(result.getSuccess());
    }
}
