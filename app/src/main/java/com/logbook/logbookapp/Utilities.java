package com.logbook.logbookapp;

import android.content.Context;
import android.widget.Spinner;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 8/12/2016.
 */
public class Utilities {

    public static int getSpinnerIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static Map<String, String> processXmlResult(String xmlStr){
        Serializer serializer = new Persister();
        Map<String,String> vinqueryInfo = new HashMap<>();
        try {

            VINquery vinQueryTests = serializer.read(VINquery.class, xmlStr);
            //VINquery vinQueryTests = (VINquery) unmarshaller.unmarshal(new StringReader(xmlStr));
            vinqueryInfo = vinQueryTests.getInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vinqueryInfo;
    }

    public static File createTempOCRFile(Context context) throws IOException {
        String imageFileName = "TempOCR";
        File storageDir = context.getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        image.deleteOnExit();
        return image;
    }

    public static File createImageFile(Context context) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VehicleIcon";
        File storageDir = context.getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
