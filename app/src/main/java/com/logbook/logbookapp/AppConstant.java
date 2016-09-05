package com.logbook.logbookapp;

/**
 * Created on 3/26/2016.
 */
public interface  AppConstant{

    String VEHICLE_TAB = "Vehicles";
    String SERVICE_SHOP_TAB = "Service Shop";
    String OTHER_TAB = "Other";
    String SERVICE_LOG_TAB = "Service Log";
    String GAS_LOG_TAB = "Gas Log";
    int REQUEST_IMAGE_CAPTURE = 101;
    int GET_OCR_File = 103;
    int SWITCH_SERVICE_CATELOG = 102;
    int OCR_TIMEOUT = 15;
    int CONNECTION_TIME_OUT = 10000;
    int VERTICAL_ITEM_SPACE = 5;


    enum AppEnum {
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

