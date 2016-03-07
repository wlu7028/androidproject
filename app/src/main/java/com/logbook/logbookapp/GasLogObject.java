package com.logbook.logbookapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 3/6/2016.
 */
public class GasLogObject {
    private String vehicle;
    private String gallon;
    private String totalCost;
    private String gasOdometer;
    private String location;
    private long userEntryTime;
    private long addGasDate;

    public long getAddGasDate() {
        return addGasDate;
    }

    public void setAddGasDate(long addGasDate) {
        this.addGasDate = addGasDate;
    }

    public long getUserEntryTime() {
        return userEntryTime;
    }

    public void setUserEntryTime(long userEntryTime) {
        this.userEntryTime = userEntryTime;
    }



    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getGallon() {
        return gallon;
    }

    public void setGallon(String gallon) {
        this.gallon = gallon;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getGasOdometer() {
        return gasOdometer;
    }

    public void setGasOdometer(String gasOdometer) {
        this.gasOdometer = gasOdometer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }






    @Override
    public String toString()
    {
        String returnJsonString = "";
        try {
            returnJsonString= new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return returnJsonString;
    }
}
