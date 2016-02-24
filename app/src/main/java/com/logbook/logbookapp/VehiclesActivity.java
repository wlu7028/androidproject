package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logbook.logbookapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehiclesActivity extends AppCompatActivity {

    private VehicleListAdapter vehicleListAdapter = null;
    private List<String> displayValues1 = new ArrayList<>();
    private List<String> displayValues2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LogBook");
        setSupportActionBar(toolbar);

        populateListView();
    }

    public void populateListView(){
        List<String> vehicleDataJsonList = ReadSaveDataUtility.readSharedPreference(this);
        List<CarObject> vehicleObjects = new ArrayList<>();
        for(String tempStr : vehicleDataJsonList){
            CarObject carObj = new CarObject();
            try {
                carObj = new ObjectMapper().readValue(tempStr,CarObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            vehicleObjects.add(carObj);
        }
        getVehicleMakerModel(vehicleObjects,displayValues1);
        getVehicleMilesAndServiceDate(vehicleObjects,displayValues2);

        ListView vehicleListView = (ListView) findViewById(R.id.vehiclelistview);
        if(vehicleListAdapter == null){
            vehicleListAdapter = new VehicleListAdapter(this,displayValues1,displayValues2);
            vehicleListView.setAdapter(vehicleListAdapter);
        }else{
            vehicleListAdapter.notifyDataSetChanged();
        }


        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }

    public void getVehicleMakerModel( List<CarObject> carObjList, List<String> displayValues1){
          displayValues1.clear();
          for(int i=0; i< carObjList.size();i++){
              displayValues1.add(carObjList.get(i).getCarMaker() +" "+carObjList.get(i).getCarModel());
          }
    }

    public void getVehicleMilesAndServiceDate( List<CarObject> carObjList,List<String> displayValues2){
        displayValues2.clear();
        for(int i=0; i< carObjList.size();i++){
            displayValues2.add(carObjList.get(i).getCarOdometer() +"miles   Last Service Date:");
        }

    }
    public void backButton(View view){
        finish();
    }

    public void addVehicle(View view){
        Intent intent = new Intent(this,AddAVehicle.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        /*
        new AlertDialog.Builder(this)
        .setTitle("Really Exit?")
        .setMessage("Are you sure you want to exit?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                WelcomeActivity.super.onBackPressed();
            }
        }).create().show();
         */
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */
}
