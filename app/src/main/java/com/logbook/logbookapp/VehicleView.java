package com.logbook.logbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.logbook.logbookapp.R;

public class VehicleView extends AppCompatActivity {
    private int rowPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");

        if(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation() == null ||
                ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation().isEmpty()){
            //default image view for now, reserve for future feature
        }else{
            ImageView  viewVehicleIcon = (ImageView) findViewById(R.id.viewVehicleIcon);
            viewVehicleIcon.setImageBitmap(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext(),
                    ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation() ));
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.viewVehicle_tab);
        tabLayout.addTab(tabLayout.newTab().setText(AppConstant.SERVICE_LOG_TAB), false);
        tabLayout.addTab(tabLayout.newTab().setText(AppConstant.GAS_LOG_TAB),false);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case AppConstant.SERVICE_LOG_TAB:
                        startServiceLogActivity();
                        break;
                    case AppConstant.GAS_LOG_TAB:
                        startGasLogActivity();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        populateView();

    }

    public void startServiceLogActivity(){
        Intent intent = new Intent(this,ServiceLog.class);
        intent.putExtra("rowPosition",rowPosition);
        startActivity(intent);
    }

    public void startGasLogActivity(){
        Intent intent = new Intent(this,GasLog.class);
        intent.putExtra("rowPosition",rowPosition);
        startActivity(intent);
    }

    public void backToVehicleLists(View view){
        finish();
    }

    public void editVehicle(View view){
        Intent intent = new Intent(this,EditVehicle.class);
        intent.putExtra("rowPosition",rowPosition);
        startActivity(intent);
    }

    /*
    @Override
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }*/
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
        ((TextView)findViewById(R.id.makerView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarMaker());
        ((TextView)findViewById(R.id.modelView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarModel());
        ((TextView)findViewById(R.id.trimView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarTrim());
        ((TextView)findViewById(R.id.yearView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarYear());
        ((TextView)findViewById(R.id.licenseView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarLicensePlateNumber());
        ((TextView)findViewById(R.id.vinView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarVIN());
        ((TextView)findViewById(R.id.odometerView)).
                setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarOdometer());
        if(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation() != null && !ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation().isEmpty()){
            ((ImageView)findViewById(R.id.viewVehicleIcon))
                    .setImageBitmap(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext()
                            , ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation()));
        }
    }
}
