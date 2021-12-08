package com.bericb.familymap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;

public class LoginTest {

    private ServerProxy serverProxy;

    @Before
    public void setUp() throws Exception {
        serverProxy = new ServerProxy();

        RegisterRequest request = new RegisterRequest("spongebob", "squidward", "sponge@bikini.bottom", "Sponge", "Bob", "m", null);

        RegisterResult result = serverProxy.register(request, "localhost", "8080");
    }

    @Test
    public void loginFail() {
        LoginRequest request = new LoginRequest("spongebob", "squid-game");
        LoginResult result = serverProxy.login(request, "localhost", "8080");

        assertEquals(false, result.getSuccess());
    }

    @Test
    public void loginSuccess() {
        LoginRequest loginRequest = new LoginRequest("spongebob", "squidward");
        LoginResult loginResult = serverProxy.login(loginRequest, "localhost", "8080");

        assertTrue(loginResult.getSuccess());
    }

    @Test
    public void loginBadPass() {
        LoginRequest request = new LoginRequest("spongebob", "squid-game");
        LoginResult result = serverProxy.login(request, "localhost", "8080");

        assertFalse(result.getSuccess());
    }

    @Test
    public void loginBadUser() {
        LoginRequest request = new LoginRequest("sponqebob", "squidward");
        LoginResult result = serverProxy.login(request, "localhost", "8080");

        assertFalse(result.getSuccess());
    }
}
