package com.bericb.familymap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import requestResult.AllPersonRequest;
import requestResult.AllPersonResult;
import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class FetchPeopleTest {

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
        AllPersonRequest request = new AllPersonRequest(UUID.randomUUID().toString());

        AllPersonResult result = serverProxy.getPeople(request, UUID.randomUUID().toString());

        assertFalse(result.getSuccess());
    }

    @Test
    public void fetchSuccess() {
        AllPersonRequest request = new AllPersonRequest(loginResult.getPersonID());
        AllPersonResult result = serverProxy.getPeople(request, loginResult.getAuthToken());

        assertTrue(result.getSuccess());
    }


}
