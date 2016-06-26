package com.logbook.logbookapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created on 6/5/2016.
 */

@XmlRootElement(name = "VINquery")
public class VINquery {
    private VINInfo vin;
    public VINquery(){}
    public VINquery(VINInfo vin){
        this.vin = vin;
    }

    @XmlElement(name = "VIN")
    public VINInfo getVin() {
        return vin;
    }

    public void setVin(VINInfo vin) {
        this.vin = vin;
    }


    public Map<String, String> getInfo(){
        Map<String,String> vinqueryInfo = new HashMap<>();
        vin.getVehicle().get(0).processItem();
        if(vin.getStatus().equalsIgnoreCase("FAILED")){
            //look up failed, we may prompt something
            Log.d("vinquery", "status is failed");
        }else{
            //update field on UI
            vinqueryInfo.put("Make", vin.getVehicle().get(0).getMake());
            vinqueryInfo.put("Model", vin.getVehicle().get(0).getModel());
            vinqueryInfo.put("Year", vin.getVehicle().get(0).getManufacturedIn());
            vinqueryInfo.put("Trim", vin.getVehicle().get(0).getTrimLevel());
        }
        return vinqueryInfo;
        /*
        System.out.println("VIN number:"+vin.getNumber());
        System.out.println("VIN Status:"+vin.getStatus());
        System.out.println("Vehicle body style:"+vin.getVehicle().get(0).getBodyStyle());
        System.out.println("Vehicle engine type:"+vin.getVehicle().get(0).getEngineType());
        System.out.println("Vehicle make:"+vin.getVehicle().get(0).getMake());
        System.out.println("Vehicle ManufacturedIn:"+vin.getVehicle().get(0).getManufacturedIn());
        System.out.println("Vehicle model:"+vin.getVehicle().get(0).getModel());
        System.out.println("Vehicle trim level:"+vin.getVehicle().get(0).getTrimLevel());
        */
    }

}
