package com.logbook.logbookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceCategories extends AppCompatActivity {
    private ServiceCategoryAdapter serviceCategoryAdapter = null;
    private String setCheckBox;
    private List<ServiceCategoryObject> serviceCategoryCheckBoxList = new ArrayList<ServiceCategoryObject>();
    public final String[] serviceCategories = {"Oil Change", "Air Filter", "Break"
    ,"Tire Rotation", "Engine","Transmission","New Tires Installation","Wheel Alignment",
    "Air Condition","Smoke Check","Battery Service","General Electronic Service","General Mechanic Service"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.APPTITLE.getText());
        setSupportActionBar(toolbar);
        setCheckBox = getIntent().getExtras().getString("setcheckbox");
        populateListView();

    }

    public void populateListView(){
        ListView serviceCategoryListView = (ListView) findViewById(R.id.servicecategorylistview);
        serviceCategoryAdapter = new ServiceCategoryAdapter(this,getModel());
        serviceCategoryListView.setAdapter(serviceCategoryAdapter);
    }

    private List<ServiceCategoryObject> getModel() {
        String[] setCheckBoxArray = setCheckBox.split(",");
        for(int i=0; i< serviceCategories.length;i++){
            if(Arrays.asList(setCheckBoxArray).contains(serviceCategories[i])){
                serviceCategoryCheckBoxList.add(getServiceCategory(serviceCategories[i],true));
            }else{
                serviceCategoryCheckBoxList.add(getServiceCategory(serviceCategories[i],false));
            }

        }
        return serviceCategoryCheckBoxList;
    }

    private ServiceCategoryObject getServiceCategory(String name,boolean isSelected) {
        return new ServiceCategoryObject(name,isSelected);
    }

    public void backToSelectCategories(View view){
        StringBuilder strBuilder = new StringBuilder(1000);
        for(ServiceCategoryObject tempServiceCategoryObj: serviceCategoryCheckBoxList){
            if(tempServiceCategoryObj.isSelected()){
                strBuilder.append(tempServiceCategoryObj.getName());
                strBuilder.append(",");
            }
        }
        if(strBuilder.length()>0)
            strBuilder.deleteCharAt(strBuilder.toString().lastIndexOf(","));
        Intent returnIntent = new Intent();
        returnIntent.putExtra("setcheckbox",strBuilder.toString());
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }



}
