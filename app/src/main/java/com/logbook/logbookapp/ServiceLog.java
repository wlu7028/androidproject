package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class ServiceLog extends AppCompatActivity {
    private int rowPosition;
    private ServiceLogListAdapter serviceLogListAdapter = null;
    private List<String> displayValues1 = new ArrayList<>();
    private List<String> displayValues2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LogBook");
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");

        populateListView();
    }

    public void populateListView(){
        ReadSaveDataUtility.loadVehicleObjectsFromSharedPreference(this);
        getServiceDateCategories(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects(), displayValues1);
        getServiceOdometers(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects(), displayValues2);

        ListView serviceLogListView = (ListView) findViewById(R.id.serviceloglistview);
        if(serviceLogListAdapter == null){
            serviceLogListAdapter = new ServiceLogListAdapter(this,displayValues1,displayValues2,rowPosition);
            serviceLogListView.setAdapter(serviceLogListAdapter);
        }else{
            serviceLogListAdapter.notifyDataSetChanged();
        }


        serviceLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                //String value = (String) adapter.getItemAtPosition(position);
                Intent viewIndividualServiceLog = new Intent(ServiceLog.this, ServiceLogView.class);
                viewIndividualServiceLog.putExtra("rowPosition", rowPosition);
                viewIndividualServiceLog.putExtra("serviceLogPosition", position);
                startActivity(viewIndividualServiceLog);
            }
        });
    }

    private void getServiceDateCategories( List<ServiceLogObject> serviceLogObjList, List<String> displayValues1){
        displayValues1.clear();
        for(int i=0; i< serviceLogObjList.size();i++){
            displayValues1.add(serviceLogObjList.get(i).getServiceDate() +" "+serviceLogObjList.get(i).getServiceCategories());
        }
    }

    private void getServiceOdometers( List<ServiceLogObject> serviceLogObjList,List<String> displayValues2){
        displayValues2.clear();
        for(int i=0; i< serviceLogObjList.size();i++){
            displayValues2.add(serviceLogObjList.get(i).getServiceOdometer() +" miles");
        }
    }

    public void addServiceLog(View view){
        Intent intent = new Intent(this,AddServiceLog.class);
        intent.putExtra("rowPosition",rowPosition);
        startActivity(intent);
    }

    public void backButton(View view){
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView();
    }
}
