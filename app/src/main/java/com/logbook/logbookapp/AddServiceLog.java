package com.logbook.logbookapp;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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

public class AddServiceLog extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText    serviceCategories;
    private TextView datePicker;
    private DatePickerDialog datePickerDialog;
    private int rowPosition;
    private String setCheckBox = "";
    List<String> receiptFileLocations = new ArrayList<>();
    List<Bitmap> screenShotsToSave = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePicker = (TextView) findViewById(R.id.saveServiceDatePicker);
        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.requestFocus();
        setDateTimeField();
        serviceCategories = (EditText) findViewById(R.id.saveServiceCategories);
        serviceCategories.setInputType(InputType.TYPE_NULL);
        serviceCategories.requestFocus();
        TextView vehicleName = (TextView) findViewById(R.id.saveServiceVehicleName);
        StringBuilder strBuilder = new StringBuilder(100);
        strBuilder.append(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarMaker());
        strBuilder.append(" ");
        strBuilder.append(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarModel());
        vehicleName.setText(strBuilder.toString());
        TextInputLayout odometerText = (TextInputLayout) findViewById(R.id.saveServiceOdometerTextInputLayout);
        //odometerText.setErrorEnabled(true);
        //odometerText.setError("Required");

        mRecyclerView = (RecyclerView) findViewById(R.id.addServiceLogRcyList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ServiceLogImagesAdapter(getBaseContext(),screenShotsToSave);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void saveServiceLog(View view){
        ServiceLogObject tempServiceLogObject = new ServiceLogObject();
        tempServiceLogObject.setVehicle(((TextView) findViewById(R.id.saveServiceVehicleName)).getText().toString());
        tempServiceLogObject.setServiceDate(((TextView) findViewById(R.id.saveServiceDatePicker)).getText().toString());
        tempServiceLogObject.setCost(((EditText) findViewById(R.id.saveServiceCost)).getText().toString());
        tempServiceLogObject.setServiceOdometer(((EditText) findViewById(R.id.saveServiceOdometer)).getText().toString());
        tempServiceLogObject.setTag(((EditText) findViewById(R.id.saveServiceTag)).getText().toString());
        tempServiceLogObject.setServiceCategories(((EditText) findViewById(R.id.saveServiceCategories)).getText().toString());
        tempServiceLogObject.setUserEntryDateTime((System.currentTimeMillis() / 1000L));
        tempServiceLogObject.setAttachmentLocation(receiptFileLocations);
        saveAllScreenShots(receiptFileLocations);
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().add(tempServiceLogObject);
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    public void cancelButton(View view){
        finish();
    }

    private void saveAllScreenShots(List<String> fileNames){
        //if(fileNames.size() == screenShotsToSave.size())
        for(int i=0;i< screenShotsToSave.size();i++){
            ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(),screenShotsToSave.get(i),fileNames.get(i));
        }
    }
    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        datePicker.setText(dateFormatter.format(newCalendar.getTime()) );
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
