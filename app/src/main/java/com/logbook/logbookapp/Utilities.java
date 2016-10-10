package com.logbook.logbookapp;

import android.content.Context;
import android.widget.Spinner;

import com.logbook.logbookapp.OCRVINResource.VINquery;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by me on 8/12/2016.
 */
public class Utilities {

    public static Map<String,List<String>> vehicleMakerModelMap = new TreeMap<>();

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

//        File image  = new File(storageDir, imageFileName+timeStamp+".jpg");
//        image.createNewFile();
        return image;
    }

    public static Map<String, List<String>> parseVehicleMakersModels(InputStream is) {
        Map<Integer, String> vehicleMakerIdMap = new TreeMap<>();
        if(vehicleMakerModelMap.isEmpty()){
            int makerId = 10;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                List<String> models = new LinkedList<>();
                while ((line = reader.readLine()) != null) {
                    if(line.length() > 0 ) {
                        models.add(line);
                    }
                    else{
                        String makerName = models.remove(0);
                        vehicleMakerIdMap.put(makerId,makerName);
                        vehicleMakerModelMap.put(makerName,models) ;
                        models = new LinkedList<>();
                    }
                    makerId += 10;
                }
                String makerName = models.remove(0);
                vehicleMakerIdMap.put(makerId,makerName);
                vehicleMakerModelMap.put(makerName,models) ;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return vehicleMakerModelMap;
    }


}
