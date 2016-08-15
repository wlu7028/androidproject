package com.logbook.logbookapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.logbook.logbookapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditServiceLog extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText    serviceCategories,serviceOdometer,serviceCost,serviceTag;
    private TextView vehicleName,datePicker;
    private DatePickerDialog datePickerDialog;
    private int rowPosition,serviceLogPosition;
    private String setCheckBox = "";
    private ServiceLogObject tempServiceLogObj;
    List<String> receiptFileLocations = new ArrayList<>();
    private List<Bitmap> screenShotsToSave = new ArrayList<>();
    private List<Bitmap> imagesToShow = new ArrayList<>();
    private List<Bitmap> previousImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        serviceLogPosition = getIntent().getExtras().getInt("serviceLogPosition");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePicker = (TextView) findViewById(R.id.editServiceDatePicker);
        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.requestFocus();
        setDatePickerDialog();
        serviceCategories = (EditText) findViewById(R.id.editServiceCategories);
        serviceCategories.setInputType(InputType.TYPE_NULL);
        serviceCategories.requestFocus();
        setValuesToFields();
        mRecyclerView = (RecyclerView) findViewById(R.id.editServiceLogRcyList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        populateImagesToShow();
        mAdapter = new ServiceLogImagesAdapter(getBaseContext(),imagesToShow);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void populateImagesToShow(){
        imagesToShow.clear();
        if(tempServiceLogObj == null && previousImages.isEmpty()){
            tempServiceLogObj = ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition);
        }else if(previousImages.isEmpty()){
            for(String tempFile : tempServiceLogObj.getAttachmentLocation()){
                previousImages.add(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext(),tempFile));
            }
        }else{
            imagesToShow.addAll(previousImages);
        }
        imagesToShow.addAll(screenShotsToSave);
    }

    private void setValuesToFields(){
        tempServiceLogObj = ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition);
        vehicleName = (TextView) findViewById(R.id.editServiceVehicleName);
        serviceOdometer = (EditText) findViewById(R.id.editServiceOdometer);
        serviceCost = (EditText) findViewById(R.id.editServiceCost);
        serviceTag = (EditText) findViewById(R.id.editServiceTag);
        datePicker.setText(tempServiceLogObj.getServiceDate());
        serviceCategories.setText(tempServiceLogObj.getServiceCategories());
        setCheckBox = tempServiceLogObj.getServiceCategories();
        vehicleName.setText(tempServiceLogObj.getVehicle());
        serviceOdometer.setText(tempServiceLogObj.getServiceOdometer());
        serviceCost.setText(tempServiceLogObj.getCost());
        serviceTag.setText(tempServiceLogObj.getTag());

    }
    public void saveServiceLog(View view){
        tempServiceLogObj.setVehicle(vehicleName.getText().toString());
        tempServiceLogObj.setServiceDate(datePicker.getText().toString());
        tempServiceLogObj.setCost(serviceCost.getText().toString());
        tempServiceLogObj.setServiceOdometer(serviceOdometer.getText().toString());
        tempServiceLogObj.setTag(serviceTag.getText().toString());
        tempServiceLogObj.setServiceCategories(serviceCategories.getText().toString());
        tempServiceLogObj.setModifiedDateTime((System.currentTimeMillis() / 1000L));
        saveAllScreenShots(receiptFileLocations);
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    public void cancelButton(View view){
        finish();
    }

    private void saveAllScreenShots(List<String> fileNames){
        //if(fileNames.size() == screenShotsToSave.size())
        for(int i=0;i< screenShotsToSave.size();i++){
            tempServiceLogObj.getAttachmentLocation().add(fileNames.get(i));
            ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(),screenShotsToSave.get(i),fileNames.get(i));
        }
    }

    private void setDatePickerDialog() {

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                datePicker.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public void showDatePickerDialog(View view){
        datePickerDialog.show();
    }

    public void showServiceCategoriesSelection(View view){
        Intent startServiceCategoriesSelection = new Intent(this,ServiceCategories.class);
        startServiceCategoriesSelection.putExtra("setcheckbox", setCheckBox);
        startActivityForResult(startServiceCategoriesSelection, AppConstant.SWITCH_SERVICE_CATELOG, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void TakeScreenShotReceipt(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utilities.createImageFile(this);
                receiptFileLocations.add(photoFile.getName());
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                startActivityForResult(takePictureIntent, AppConstant.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateImagesToShow();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case AppConstant.REQUEST_IMAGE_CAPTURE:
                    screenShotsToSave.add((Bitmap) extras.get("data"));
                    break;
                case AppConstant.SWITCH_SERVICE_CATELOG:
                    setCheckBox = data.getExtras().getString("setcheckbox");
                    serviceCategories.setText(setCheckBox);
                    break;
                default:
                    break;
            }
        } else {
            // result is not ok
            receiptFileLocations.remove(receiptFileLocations.size() - 1);
            Log.d("cancel", "return code is not ok");
        }
    }



}
