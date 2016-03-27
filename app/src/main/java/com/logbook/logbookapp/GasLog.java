package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.logbook.logbookapp.R;

import java.util.ArrayList;
import java.util.List;

public class GasLog extends AppCompatActivity {
    private int rowPosition;
    private GasLogListAdapter gasLogListAdapter = null;
    private List<String> displayValues1 = new ArrayList<>();
    private List<String> displayValues2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");

        populateListView();

    }

    public void populateListView() {
        ReadSaveDataUtility.loadVehicleObjectsFromSharedPreference(this);
        getAddGasDate(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects(), displayValues1);
        getGallonsOdometersPrice(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects(), displayValues2);

        ListView gasLogListView = (ListView) findViewById(R.id.gasloglistview);
        if (gasLogListAdapter == null) {
            gasLogListAdapter = new GasLogListAdapter(this, displayValues1, displayValues2,rowPosition);
            gasLogListView.setAdapter(gasLogListAdapter);
        } else {
            gasLogListAdapter.notifyDataSetChanged();
        }


        gasLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                //String value = (String) adapter.getItemAtPosition(position);
                Intent viewIndividualGasLog = new Intent(GasLog.this, GasLogView.class);
                viewIndividualGasLog.putExtra("rowPosition", rowPosition);
                viewIndividualGasLog.putExtra("gasLogPosition", position);
                startActivity(viewIndividualGasLog);
            }
        });
    }

    private void getAddGasDate(List<GasLogObject> gasLogObjList, List<String> displayValues1) {
        displayValues1.clear();
        for (int i = 0; i < gasLogObjList.size(); i++) {
            displayValues1.add(gasLogObjList.get(i).getAddGasDate());
        }
    }

    private void getGallonsOdometersPrice(List<GasLogObject> gasLogObjList, List<String> displayValues2) {
        displayValues2.clear();
        for (int i = 0; i < gasLogObjList.size(); i++) {
            float costPerGallon = (float) (Float.parseFloat(gasLogObjList.get(i).getTotalCost()) / Float.parseFloat(gasLogObjList.get(i).getGallon()));
            displayValues2.add(gasLogObjList.get(i).getGallon() + " Gallon   " + gasLogObjList.get(i).getGasOdometer()
                    + " miles   $" + String.format("%.2f", costPerGallon));
        }
    }

    public void addGasLog(View view) {
        Intent intent = new Intent(this, AddGasLog.class);
        intent.putExtra("rowPosition", rowPosition);
        startActivity(intent);
    }

    public void backButton(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView();
    }
}

