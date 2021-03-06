package com.logbook.logbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


import com.logbook.logbookapp.OCRVINResource.RestServiceUtility;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AddAVehicle extends AppCompatActivity  {

    Spinner spinner1,spinner2;
    String carPicFileName = "";
    ImageButton carPicButton;
    Bitmap changeVehicleIcon= null;
    private boolean ocrPhotoCompleted = false;
    private ProgressDialog pd;
    private ImageButton getOCRButton;
    private String ocrResult,ocrTempFileLocation;
    ArrayAdapter<String> adapter1,adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);

        final Map<String, List<String>> vehicleMakerModelMap = Utilities.parseVehicleMakersModels(getResources().openRawResource(R.raw.vehicle_makers_models));

        carPicButton = (ImageButton) findViewById(R.id.vehiclePicButton);
        spinner1 = (Spinner) findViewById(R.id.maker);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                vehicleMakerModelMap.keySet().toArray(new String[vehicleMakerModelMap.keySet().size()]));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner2 = (Spinner) findViewById(R.id.model);
        adapter2 =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.car_model));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                String carModel = parentView.getItemAtPosition(position).toString();
                adapter2 =  new ArrayAdapter<>(selectedItemView.getContext(), android.R.layout.simple_spinner_item,
                        vehicleMakerModelMap.get(carModel) );

                spinner2.setAdapter(adapter2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });



        getOCRButton = (ImageButton) findViewById(R.id.getOCRAddV);
        getOCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Utilities.checkPlayServices((Activity) v.getContext());
                getOCRPhoto();
                v.setEnabled(false);
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                Map<String,String> vinqueryInfo = new HashMap<>();
                    @Override
                    protected void onPreExecute() {
                        pd = new ProgressDialog(AddAVehicle.this);
                        pd.setTitle("Processing OCR...");
                        pd.setMessage("Please wait.");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        int tried = 0;
                        try {
                            while (!ocrPhotoCompleted && ++tried < AppConstant.OCR_TIMEOUT) {
                                Log.d("ocrworker", "in while, tried=" + tried);
                                Thread.sleep(2000);
                            }
                            // test google ocr
                            if(ocrPhotoCompleted && ocrTempFileLocation != null)
                                ocrResult = RestServiceUtility.googleMobileVisionOCRProcess(getBaseContext(),ocrTempFileLocation);
                            else
                                Log.d("ocrworker", "no picture is taken for VIN lookup");
                            if(ocrResult !=null && !ocrResult.isEmpty())
                                vinqueryInfo = Utilities.processXmlResult(RestServiceUtility.processVIN(ocrResult));
                            else
                                Log.d("ocrworker", "vin number is empty from ocr detection");

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        //update UI
                        if(ocrResult == null || ocrResult.isEmpty()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            //builder.setMessage("View image or delete image");
                            builder.setTitle("Unable to detect VIN Number");
                            builder.setCancelable(true);
                            builder.setPositiveButton(
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else
                            ((EditText) findViewById(R.id.vin)).setText(ocrResult);
                        if(!vinqueryInfo.isEmpty()){
                            Log.d("ocrSet","set spinner");
                            spinner1.setSelection(Utilities.getSpinnerIndex(spinner1, vinqueryInfo.get("Make")));
                            spinner2.setSelection(Utilities.getSpinnerIndex(spinner2, vinqueryInfo.get("Model")));
                            ((EditText) findViewById(R.id.year)).setText(vinqueryInfo.get("Year"));
                        }
                        if (pd != null) {
                            pd.dismiss();
                            getOCRButton.setEnabled(true);
                        }
                    }

                };
                task.execute((Void[]) null);

            }
        });

    }

    public void saveVehicle(View view){
        //SharedPreferences sp = this.getSharedPreferences("CarDataSharedPref",Context.MODE_PRIVATE);
       // List<String> carDataSP = ReadSaveDataUtility.readSharedPreference(this);
        CarObject carObj = new CarObject();
        carObj.setCarMaker(((Spinner) findViewById(R.id.maker)).getSelectedItem().toString());
        carObj.setCarModel(((Spinner) findViewById(R.id.model)).getSelectedItem().toString());
        carObj.setCarYear(((EditText) findViewById(R.id.year)).getText().toString());
        carObj.setCarLicensePlateNumber(((EditText) findViewById(R.id.licenseplatenumber)).getText().toString());
        carObj.setCarVIN(((EditText) findViewById(R.id.vin)).getText().toString());
        carObj.setCarOdometer(((EditText) findViewById(R.id.odometer)).getText().toString());
        carObj.setCarPicFileLocation(carPicFileName);
        carObj.setCreatedTimeStamp(System.currentTimeMillis() / 1000L);
        if(changeVehicleIcon != null && !carPicFileName.isEmpty())
            ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), changeVehicleIcon, carPicFileName);
        ReadSaveDataUtility.vehicleObjects.add(carObj);

        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }



    public void cancelButton(View view){
        finish();
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utilities.createImageFile(this);
                carPicFileName = "Real_"+photoFile.getName();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                //        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, AppConstant.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void getOCRPhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utilities.createTempOCRFile(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ocrTempFileLocation = photoFile.getAbsolutePath();
            Log.d("ocrFile", ocrTempFileLocation);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.logbook.logbookapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, AppConstant.GET_OCR_File);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    changeVehicleIcon = (Bitmap) extras.get("data");
                    carPicButton.setImageBitmap(changeVehicleIcon);
                    break;
                case AppConstant.GET_OCR_File:
                    ocrPhotoCompleted = true;
                    break;
                default:
                    carPicFileName = "";
                    break;
            }
        }else{
            // result is not ok
            carPicFileName = "";
            ocrPhotoCompleted = true;
            Log.d("cancel", "return code is not ok");
        }
    }





}
