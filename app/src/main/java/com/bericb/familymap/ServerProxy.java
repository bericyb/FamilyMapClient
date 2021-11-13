package com.bericb.familymap;

import requestResult.AllEventRequest;
import requestResult.AllEventResult;
import requestResult.AllPersonRequest;
import requestResult.AllPersonResult;
import requestResult.LoginRequest;
import requestResult.LoginResult;
import requestResult.RegisterRequest;
import requestResult.RegisterResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerProxy {

    private static Gson myGson = new Gson();

    LoginResult login (LoginRequest request) {
        try {
            URL url = new URL("http://localhost:8080/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqData =
                    "{" +
                        "\"username\": \"" + request.getUsername() + "\"" +
                        "\"password\": \"" + request.getPassword() + "\"" +
                    '}';

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                LoginResult result = myGson.fromJson(respData, LoginResult.class);
                System.out.println(respData);
                return result;
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                LoginResult result = myGson.fromJson(respData, LoginResult.class);
                System.out.println(respData);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    RegisterResult register(RegisterRequest request) {
        try {
            URL url = new URL("http://localhost:8080/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqData =
                    "{" +
                        "\"username\": \"" + request.getUsername() + "\"" +
                        "\"password\": \"" + request.getPassword() + "\"" +
                        "\"email\": \"" + request.getEmail() + "\"" +
                        "\"firstName\": \"" + request.getFirstName() + "\"" +
                        "\"lastName\": \"" + request.getLastName() + "\"" +
                        "\"gender\": \"" + request.getGender() + "\"" +
                    '}';

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResult result = myGson.fromJson(respData, RegisterResult.class);
                System.out.println(respData);
                return result;
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                RegisterResult result = myGson.fromJson(respData, RegisterResult.class);
                System.out.println(respData);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    AllPersonResult getPeople(AllPersonRequest request) {
        return null;
    }
    AllEventResult getEvents(AllEventRequest request) {
        return null;
    }



    /*
		The readString method shows how to read a String from an InputStream.
	*/
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
