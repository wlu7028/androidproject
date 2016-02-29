package com.logbook.logbookapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created on 2/21/2016.
 */
public class CarObject implements Comparable<CarObject>{
    private String Id;
    private String carName;
    private String carMaker;
    private String carModel;
    private String carTrim;
    private String carYear;
    private String carLicensePlateNumber;
    private String carVIN;
    private String carOdometer;
    private long createdTimeStamp;

    public CarObject(){

    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCarMaker() {
        return carMaker;
    }

    public void setCarMaker(String carMaker) {
        this.carMaker = carMaker;
    }

    public String getCarOdometer() {
        return carOdometer;
    }

    public void setCarOdometer(String carOdometer) {
        this.carOdometer = carOdometer;
    }

    public String getCarVIN() {
        return carVIN;
    }

    public void setCarVIN(String carVIN) {
        this.carVIN = carVIN;
    }

    public String getCarLicensePlateNumber() {
        return carLicensePlateNumber;
    }

    public void setCarLicensePlateNumber(String carLicensePlateNumber) {
        this.carLicensePlateNumber = carLicensePlateNumber;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarTrim() {
        return carTrim;
    }

    public void setCarTrim(String carTrim) {
        this.carTrim = carTrim;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    @Override
    public int compareTo(CarObject compareCarObject){
        long compareDisplayOrder = compareCarObject.getCreatedTimeStamp();
        return (int)(this.createdTimeStamp - compareDisplayOrder); // @TODO need a better way to handle future time, int is not large enough
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
