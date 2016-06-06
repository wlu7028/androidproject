package com.logbook.logbookapp;
import javax.xml.bind.annotation.XmlAttribute;
/**
 * Created on 6/5/2016.
 */
public class VinItem {
    private String key;
    private String value;

    public VinItem(){}
    public VinItem(String key, String value){
        this.key = key;
        this.value = value;
    }

    @XmlAttribute(name = "Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @XmlAttribute(name = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



}
