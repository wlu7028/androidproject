package com.logbook.logbookapp;



import com.logbook.logbookapp.OCRVINResource.VinItem;

import org.simpleframework.xml.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/5/2016.
 */
public class VehicleVinInfo {
    private String modelYear;
    private String Make;
    private String Model;
    private String TrimLevel;
    private String ManufacturedIn;
    private String bodyStyle;
    private String engineType;
    private List<VinItem> item;
    private Map<String,String> itemMap = new HashMap<>();

    public VehicleVinInfo(){}
    public VehicleVinInfo(String modelYear,String Make,String Model,String TrimLevel,String ManufactureIn,String bodyStyle,String engineType){
        this.modelYear = modelYear;
        this.Make = Make;
        this.Model = Model;
        this.TrimLevel = TrimLevel;
        this.ManufacturedIn = ManufactureIn;
        this.bodyStyle=bodyStyle;
        this.engineType=engineType;
    }

    @Element(name = "Item")
    public List<VinItem> getItem() {
        return item;
    }

    public void setItem(List<VinItem> item) {
        this.item = item;
    }

    public String getEngineType() {
        if(engineType == null || engineType.isEmpty()){
            engineType = itemMap.get("Engine Type");
        }
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getModelYear() {
        if(modelYear == null || modelYear.isEmpty()){
            modelYear = itemMap.get("Model Year");
        }
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getMake() {
        if(Make == null || Make.isEmpty()){
            Make = itemMap.get("Make");
        }
        return Make;
    }

    public void setMake(String make) {
        Make = make;
    }

    public String getModel()
    {
        if(Model == null || Model.isEmpty()){
            Model = itemMap.get("Model");
        }
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getTrimLevel() {
        if(TrimLevel == null || TrimLevel.isEmpty()){
            TrimLevel = itemMap.get("Trim Level");
        }
        return TrimLevel;
    }

    public void setTrimLevel(String trimLevel) {
        TrimLevel = trimLevel;
    }

    public String getManufacturedIn() {
        if(ManufacturedIn == null || ManufacturedIn.isEmpty()){
            ManufacturedIn = itemMap.get("Manufactured in");
        }
        return ManufacturedIn;
    }

    public void setManufacturedIn(String manufacturedId) {
        ManufacturedIn = manufacturedId;
    }

    public String getBodyStyle() {
        if(bodyStyle == null || bodyStyle.isEmpty()){
            bodyStyle = itemMap.get("Body Style");
        }
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public void processItem(){
        for(VinItem i : item){
            itemMap.put(i.getKey(),i.getValue());
        }
    }


}
