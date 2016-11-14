package com.logbook.logbookapp.OCRVINResource;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.logbook.logbookapp.AppConstant;

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
    private static final String vinLookupUrl = "http://ws.vinquery.com/restxml.aspx?accesscode=9c141b23-9ef9-4549-8b4b-5e1dc67d04cf&reportType=3&vin=";



    public static String googleMobileVisionOCRProcess(Context context,String filePath){
        String vinNumberFromPic = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        if (textRecognizer.isOperational()) {
            textRecognizer.setProcessor(new OcrDetectorProcessor());
            Frame imageFrame = new Frame.Builder()
                    .setBitmap( BitmapFactory.decodeFile(filePath))
                    .build();
            textRecognizer.receiveFrame(imageFrame);
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.valueAt(i); //textBlocks.get(textBlocks.keyAt(i));
                if(textBlock != null && textBlock.getValue() != null){
                    vinNumberFromPic = textBlock.getValue();
                    Log.d("OcrDetectorProcessor", "Text detected! " + vinNumberFromPic);
                }
                // Do something with value
            }
        }else{
            Log.e("OcrDetectorProcessor", "ocr is not available");
        }
        textRecognizer.release();
        return vinNumberFromPic;
    }


    public static String processOCR(String filePath){
        String charset = "UTF-8";
        String response = "";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(ocrUrl, charset);
            multipart.addFormField("language", "en");
            multipart.addFormField("apikey", ocrAPIKey);
            multipart.addFilePart("image", new File(filePath));
            response  = multipart.finish(); // response from server.
        } catch (Exception e) {
            Log.e("processVin", "processOCR fails");
        }
        return response;
    }

    public static String processVIN(String vinNumber){
        StringBuilder response = new StringBuilder();
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL(vinLookupUrl+vinNumber);
            con = (HttpURLConnection) obj.openConnection();
            con.setConnectTimeout(AppConstant.CONNECTION_TIME_OUT);
            con.setReadTimeout(AppConstant.CONNECTION_TIME_OUT);
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
            } else {
                Log.e("processVin","GET request not worked");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return response.toString();
    }
}
