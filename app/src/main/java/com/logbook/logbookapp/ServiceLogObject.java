package com.logbook.logbookapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Created on 3/5/2016.
 */
public class ServiceLogObject implements Comparable<ServiceLogObject>{
    private String cost;
    private String vehicle;
    private String tag;
    private String serviceOdometer;
    private long userEntryDateTime;  // system auto-insert timestamp when user save this entry
    private long modifiedDateTime;  // system auto-insert timestamp when user save this entry
    private String serviceDate;
    private String serviceCategories;
    private List<String> attachmentLocation;

    public ServiceLogObject(){

    }

    public List<String> getAttachmentLocation() {
        return attachmentLocation;
    }

    public void setAttachmentLocation(List<String> attachmentLocation) {
        this.attachmentLocation = attachmentLocation;
    }


    public long getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(long modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(String serviceCategories) {
        this.serviceCategories = serviceCategories;
    }

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

    public long getUserEntryDateTime() {
        return userEntryDateTime;
    }

    public void setUserEntryDateTime(long userEntryDateTime) {
        this.userEntryDateTime = userEntryDateTime;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
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

    @Override
    public int compareTo(ServiceLogObject another) {
        return (int) (this.getModifiedDateTime() - another.getModifiedDateTime());
    }
}
