package com.logbook.logbookapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created  on 3/26/2016.
 */
public class RestServiceUtility {
    private static final String ocrUrl = "http://api.ocrapiservice.com/1.0/rest/ocr";
    private static final String ocrAPIKey = "XL8FySLPSG";
    private static final String vinLookupUrl = "";

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


    public static String processOCR(String filePath){

        String charset = "UTF-8";
        String response = "";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(ocrUrl, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        multipart.addFormField("language", "en");
        multipart.addFormField("apikey", ocrAPIKey);
        try {
            multipart.addFilePart("image", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response  = multipart.finish(); // response from server.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String processVIN(){
        StringBuilder response = new StringBuilder();

        return response.toString();
    }
}
