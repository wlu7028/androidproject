package com.logbook.logbookapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2/21/2016.
 */
public class ReadSaveDataUtility {
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
}
