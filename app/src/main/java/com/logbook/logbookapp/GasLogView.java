package com.logbook.logbookapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.logbook.logbookapp.R;

public class GasLogView extends AppCompatActivity {
    private int rowPosition;
    private int gasLogPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_log_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LogBook");
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        gasLogPosition = getIntent().getExtras().getInt("gasLogPosition");
        populateView();

    }

    public void backToGasLogLists(View view){
        finish();
    }

    public void editGasLog(View view){

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
        ((TextView)findViewById(R.id.viewgaslogvehiclename)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getVehicle());
        ((TextView)findViewById(R.id.viewgaslogdatepicker)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getAddGasDate());
        ((TextView)findViewById(R.id.viewgasloggallon)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getGallon());
        ((TextView)findViewById(R.id.viewgaslogtotalcost)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getTotalCost());
        ((TextView)findViewById(R.id.viewgaslogodometers)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getGasOdometer());
        ((TextView)findViewById(R.id.viewgasloglocation)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition).getLocation());
    }

}
