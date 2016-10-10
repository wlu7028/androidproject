package com.logbook.logbookapp.OCRVINResource;

import com.logbook.logbookapp.VehicleVinInfo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


import java.util.List;

/**
 * Created on 6/5/2016.
 */
public class VINInfo {
    private List<VehicleVinInfo> vehicle;
    private String Number;
    private String Status;

    public VINInfo(){}
    public VINInfo(List<VehicleVinInfo> vehicle){
        this.vehicle=vehicle;
    }

    @Element(name = "Vehicle")
    public List<VehicleVinInfo> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<VehicleVinInfo> vehicle) {
        this.vehicle = vehicle;
    }

    @Attribute(name = "Number")
    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    @Attribute(name = "Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }



}
