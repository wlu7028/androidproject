package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.logbook.logbookapp.R;

public class ServiceLogView extends AppCompatActivity {
    private int rowPosition;
    private int serviceLogPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_log_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LogBook");
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        serviceLogPosition = getIntent().getExtras().getInt("serviceLogPosition");
        populateView();


    }

    public void backToServiceLogLists(View view){
        finish();
    }

    public void editServiceLog(View view){
        Intent startEditServiceLog = new Intent(this,EditServiceLog.class);
        startEditServiceLog.putExtra("rowPosition",rowPosition);
        startEditServiceLog.putExtra("serviceLogPosition",serviceLogPosition);
        startActivity(startEditServiceLog);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateView();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void populateView(){
        ((TextView)findViewById(R.id.viewServiceVehicleName)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getVehicle());
        ((TextView)findViewById(R.id.viewServiceDatePicker)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getServiceDate());
        ((TextView)findViewById(R.id.viewServiceCost)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getCost());
        ((TextView)findViewById(R.id.viewServiceCategories)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getServiceCategories());
        ((TextView)findViewById(R.id.viewServiceOdometer)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getServiceOdometer());
        ((TextView)findViewById(R.id.viewServiceTag)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition).getTag());
    }
}
