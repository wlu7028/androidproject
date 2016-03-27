package com.logbook.logbookapp;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created  on 3/26/2016.
 */
public class RestServiceUtility {
    private final String ocrUrl = "http://www.ocrwebservice.com/restservices/processDocument?";
    private final String ocrUser ="evsdbtest";
    private final String ocrPassCode = "A628654B-07A1-4902-A252-9F6DF01F48A0";
    private final String vinLookupUrl = "";

    public String getRequest(){
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL("http://localhost:8080/RESTfulExample/json/product/get");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String lines;
            while ((lines = br.readLine()) != null) {
                output.append(lines);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


    public String postRequest(String input){
        StringBuilder output = new StringBuilder();
        try {

            URL url = new URL(ocrUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((ocrUser + ":" + ocrPassCode).getBytes(), Base64.DEFAULT));
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            int httpCode = conn.getResponseCode();
            if (httpCode == HttpURLConnection.HTTP_OK)
            {
                // Get response stream


                // Parse and print response from OCR server

            }
            else if (httpCode == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                System.out.println("OCR Error Message: Unauthorizied request");
            }
            else
            {
                // Error occurred

            }
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String lines;
            while ((lines = br.readLine()) != null) {
                output.append(lines);
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
