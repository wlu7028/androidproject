package com.logbook.logbookapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 3/5/2016.
 */
public class ServiceLogObject {
    private String cost;
    private String vehicle;
    private String tag;
    private String serviceOdometer;
    private long userEntryDate;
    private long serviceDate;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getServiceOdometer() {
        return serviceOdometer;
    }

    public void setServiceOdometer(String serviceOdometer) {
        this.serviceOdometer = serviceOdometer;
    }

    public long getUserEntryDate() {
        return userEntryDate;
    }

    public void setUserEntryDate(long userEntryDate) {
        this.userEntryDate = userEntryDate;
    }

    public long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(long serviceDate) {
        this.serviceDate = serviceDate;
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
