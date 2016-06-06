package com.logbook.logbookapp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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

    @XmlElement(name = "Vehicle")
    public List<VehicleVinInfo> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<VehicleVinInfo> vehicle) {
        this.vehicle = vehicle;
    }

    @XmlAttribute(name = "Number")
    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    @XmlAttribute(name = "Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }



}
