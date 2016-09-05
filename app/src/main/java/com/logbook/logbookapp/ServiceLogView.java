package com.logbook.logbookapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.logbook.logbookapp.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceLogView extends AppCompatActivity {
    private int rowPosition;
    private int serviceLogPosition;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_log_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        serviceLogPosition = getIntent().getExtras().getInt("serviceLogPosition");
        populateView();

        mRecyclerView = (RecyclerView) findViewById(R.id.rcyList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(AppConstant.VERTICAL_ITEM_SPACE));
        List<String> imagePaths = ReadSaveDataUtility.vehicleObjects.get(rowPosition).
                getServiceLogObjects().get(serviceLogPosition).getAttachmentLocation();
        List<Bitmap> imagesToShow = new ArrayList<>();
        for(String tempPath : imagePaths){
            imagesToShow.add(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext(),tempPath));
        }
        mAdapter = new ServiceLogImagesAdapter(getBaseContext(),imagesToShow);
        mRecyclerView.setAdapter(mAdapter);
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
