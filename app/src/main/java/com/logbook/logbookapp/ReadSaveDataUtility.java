package com.logbook.logbookapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created  on 2/21/2016.
 */
public class ReadSaveDataUtility {
    public static  List<CarObject> vehicleObjects = new ArrayList<>();

    public static void loadVehicleObjectsFromSharedPreference(Activity thisActivity){
        vehicleObjects.clear();
        List<String> vehicleDataJsonList = readSharedPreference(thisActivity);
        for(String tempStr : vehicleDataJsonList){
            CarObject carObj = new CarObject();
            try {
                carObj = new ObjectMapper().readValue(tempStr,CarObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            vehicleObjects.add(carObj);
        }
        Collections.sort(vehicleObjects);
    }

    public static List<String> readSharedPreference(Activity thisActivity){
        SharedPreferences sp = thisActivity.getSharedPreferences("CarDataSharedPref",Context.MODE_PRIVATE);
        List<String> carDataSP = new ArrayList<>();
        int size = sp.getInt("CarData_size", 0);
        for(int i=0;i<size;i++)
        {
            if(sp.getString("CarData_" + i,null) != null)
                carDataSP.add(sp.getString("CarData_" + i, null));
        }
        return carDataSP;
    }

    public static void deleteVehicle(Activity thisActivity, int deletePosition){
        vehicleObjects.remove(deletePosition);
        saveVehicleListToSharedPreference(thisActivity);
    }

    public static void saveVehicleListToSharedPreference(Activity thisActivity){
        SharedPreferences sp = thisActivity.getSharedPreferences("CarDataSharedPref",Context.MODE_PRIVATE);
        List<String> newCarDataList = new ArrayList<>();
        for(CarObject carObj : vehicleObjects){
            newCarDataList.add(carObj.toString());
        }
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.clear();
        mEdit1.putInt("CarData_size", newCarDataList.size());
        for(int i=0;i<newCarDataList.size();i++)
        {
            mEdit1.putString("CarData_" + i, newCarDataList.get(i));
        }
        mEdit1.commit();
    }
}
