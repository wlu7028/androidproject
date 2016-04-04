package com.logbook.logbookapp;

/**
 * Created on 3/26/2016.
 */
public interface  AppConstant{

    public static final String VEHICLE_TAB = "Vehicles";
    public static final String SERVICE_SHOP_TAB = "Service Shop";
    public static final String OTHER_TAB = "Other";
    public static final String SERVICE_LOG_TAB = "Service Log";
    public static final String GAS_LOG_TAB = "Gas Log";
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int GET_OCR_File = 103;


    public enum AppEnum {
        APPTITLE("EVSDB");

        private String actualText;

        AppEnum(String text){
            this.actualText = text;
        }

        public String getText(){
            return actualText;
        }
    }
}

