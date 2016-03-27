package com.logbook.logbookapp;

/**
 * Created on 3/26/2016.
 */
public enum AppConstant {
     APPTITLE("EVSDB"),
     VEHICLE_TAB("Vehicles"),
     SERVICE_SHOP_TAB("Service Shop"),
     OTHER_TAB("Other"),
     SERVICE_LOG_TAB("Service Log"),
     GAS_LOG_TAB("Gas Log");

    private String actualText;

    AppConstant(String text){
        this.actualText = text;
    }

    public String getText(){
        return actualText;
    }
}
