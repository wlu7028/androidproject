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
import java.util.Collections;
import java.util.List;

public class VehiclesActivity extends AppCompatActivity {

    private VehicleListAdapter vehicleListAdapter = null;
    private List<String> displayValues1 = new ArrayList<>();
    private List<String> displayValues2 = new ArrayList<>();
    private List<String> vehicleIcons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);

        populateListView();
    }

    public void populateListView(){
        ReadSaveDataUtility.loadVehicleObjectsFromSharedPreference(this);
        getVehicleMakerModel(ReadSaveDataUtility.vehicleObjects, displayValues1);
        getVehicleMilesAndServiceDate(ReadSaveDataUtility.vehicleObjects, displayValues2);
        getVehicleIcon(ReadSaveDataUtility.vehicleObjects,vehicleIcons);
        ListView vehicleListView = (ListView) findViewById(R.id.vehiclelistview);
        if(vehicleListAdapter == null){
            vehicleListAdapter = new VehicleListAdapter(this,displayValues1,displayValues2,vehicleIcons);
            vehicleListView.setAdapter(vehicleListAdapter);
        }else{
            vehicleListAdapter.notifyDataSetChanged();
        }


        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                //String value = (String) adapter.getItemAtPosition(position);
                Intent viewIndividualVehicle = new Intent(VehiclesActivity.this, VehicleView.class);
                viewIndividualVehicle.putExtra("rowPosition", position);
                startActivity(viewIndividualVehicle);
            }
        });
    }
    private void getVehicleIcon(List<CarObject> carObjList, List<String> vehicleIcons){
        vehicleIcons.clear();
        for(int i=0; i< carObjList.size();i++){
            if(carObjList.get(i).getCarPicFileLocation() == null || carObjList.get(i).getCarPicFileLocation().isEmpty())
                vehicleIcons.add("");
            else
                vehicleIcons.add(carObjList.get(i).getCarPicFileLocation());
        }
    }

    private void getVehicleMakerModel( List<CarObject> carObjList, List<String> displayValues1){
          displayValues1.clear();
          for(int i=0; i< carObjList.size();i++){
              displayValues1.add(carObjList.get(i).getCarMaker() +" "+carObjList.get(i).getCarModel());
          }
    }

    private void getVehicleMilesAndServiceDate( List<CarObject> carObjList,List<String> displayValues2){
        displayValues2.clear();
        for(int i=0; i< carObjList.size();i++){
            Collections.sort(carObjList.get(i).getServiceLogObjects());
            displayValues2.add(carObjList.get(i).getCarOdometer() +" miles   Last Service Date: "
                    + carObjList.get(i).getServiceLogObjects().get(0)
                     );
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
