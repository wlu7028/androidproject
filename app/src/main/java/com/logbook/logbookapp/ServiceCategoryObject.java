package com.logbook.logbookapp;

/**
 * Created on 3/14/2016.
 */
public class ServiceCategoryObject {

    private String name;
    private boolean selected;

    public ServiceCategoryObject(String name, boolean selected){
        this.name = name;
        this.selected = selected;
    }
    public ServiceCategoryObject(String name){
        this(name,false);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }




}
