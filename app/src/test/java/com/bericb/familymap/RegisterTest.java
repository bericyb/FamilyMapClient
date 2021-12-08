package com.bericb.familymap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class RegisterTest {

    private ServerProxy serverProxy;

    @Before
    public void setUp() throws Exception {
       serverProxy = new ServerProxy();
    }

    @Test
    public void regFail() {

        String randomUser = UUID.randomUUID().toString();

        RegisterRequest request = new RegisterRequest(randomUser, "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);
        RegisterResult result = serverProxy.register(request, "localhost", "8080");

        RegisterRequest badRequest = new RegisterRequest(randomUser, "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);
        RegisterResult badResult = serverProxy.register(badRequest, "localhost", "8080");

        assertTrue(result.getSuccess());

        assertFalse(badResult.getSuccess());
    }

    @Test
    public void regSuccess() {
        String randomUser = UUID.randomUUID().toString();

        RegisterRequest request = new RegisterRequest(randomUser, "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);

        RegisterResult result = serverProxy.register(request, "localhost", "8080");

        assertTrue(result.getSuccess());

        LoginRequest loginRequest = new LoginRequest(randomUser, "squidward");
        LoginResult loginResult = serverProxy.login(loginRequest, "localhost", "8080");

        assertTrue(loginResult.getSuccess());

    }

}
