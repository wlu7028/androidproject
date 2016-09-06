package com.logbook.logbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;



import java.io.File;
import java.io.IOException;

import java.util.Map;



public class AddAVehicle extends AppCompatActivity  {

    Spinner spinner1,spinner2;
    String carPicFileName = "";
    ImageButton carPicButton;
    Bitmap changeVehicleIcon= null;
    private boolean ocrPhotoCompleted = false;
    private ProgressDialog pd;
    private EditText Vin;
    private Button getOCRButton;
    private String ocrResult,ocrTempFileLocation;
    ArrayAdapter<CharSequence> adapter1,adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);

        carPicButton = (ImageButton) findViewById(R.id.vehiclePicButton);
        spinner1 = (Spinner) findViewById(R.id.maker);
        adapter1 = ArrayAdapter.createFromResource(this,
                R.array.car_maker, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner2 = (Spinner) findViewById(R.id.model);
        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.car_model, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                String carModel = parentView.getItemAtPosition(position).toString();
                switch (carModel) {
                    case "Honda":
                        adapter2 = ArrayAdapter.createFromResource(AddAVehicle.this,
                                R.array.honda_model, android.R.layout.simple_spinner_item);
                        break;
                    case "Toyota":
                        adapter2 = ArrayAdapter.createFromResource(AddAVehicle.this,
                                R.array.toyota_model, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        adapter2 = ArrayAdapter.createFromResource(AddAVehicle.this,
                                R.array.car_model, android.R.layout.simple_spinner_item);
                        break;
                }

                spinner2.setAdapter(adapter2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });
        Vin = (EditText) findViewById(R.id.vin);

        Vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Choose way of data entry: take a photo or enter manually");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Take a photo of VIN",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                                    @Override
                                    protected void onPreExecute() {
                                        pd = new ProgressDialog(AddAVehicle.this);
                                        pd.setTitle("Processing OCR...");
                                        pd.setMessage("Please wait.");
                                        pd.setCancelable(false);
                                        pd.setIndeterminate(true);
                                        pd.show();
                                        getOCRPhoto();
                                    }

                                    @Override
                                    protected Void doInBackground(Void... arg0) {
                                        int tried = 0;
                                        try {
                                            while (!ocrPhotoCompleted && ++tried < AppConstant.OCR_TIMEOUT) {
                                                Thread.sleep(2000);
                                            }
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void result) {
                                        if (pd != null) {
                                            pd.dismiss();
                                            getOCRButton.setEnabled(true);
                                        }
                                    }

                                };
                                task.execute((Void[]) null);
                            }
                        });

                builder.setNegativeButton(
                        "Enter manually",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = builder.create();
                alert1.show();
            }
        });
        getOCRButton = (Button) findViewById(R.id.getOCRAddV);
        getOCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        pd = new ProgressDialog(AddAVehicle.this);
                        pd.setTitle("Processing OCR...");
                        pd.setMessage("Please wait.");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
                        getOCRPhoto();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        int tried = 0;
                        try {
                            while (!ocrPhotoCompleted && ++tried < AppConstant.OCR_TIMEOUT) {
                                Log.d("ocradd", "in while, tried=" + tried);
                                Thread.sleep(2000);
                            }
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
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
            startActivityForResult(takePictureIntent, AppConstant.GET_OCR_File);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case AppConstant.REQUEST_IMAGE_CAPTURE:
                    changeVehicleIcon = (Bitmap) extras.get("data");
                    carPicButton.setImageBitmap(changeVehicleIcon);
                    break;
                case AppConstant.GET_OCR_File:
                    try {
                        File photoFile = Utilities.createTempOCRFile(this);
                        ocrTempFileLocation = photoFile.getAbsolutePath();
                        ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), (Bitmap) extras.get("data"), photoFile.getName());
                        ocrResult = RestServiceUtility.processOCR(ocrTempFileLocation);
                        ((EditText) findViewById(R.id.vin)).setText(ocrResult);
                        Map<String,String> vinqueryInfo = Utilities.processXmlResult(RestServiceUtility.processVIN(ocrResult));
                        //update UI
                        if(!vinqueryInfo.isEmpty()){
                            spinner1.setSelection(Utilities.getSpinnerIndex(spinner1, vinqueryInfo.get("Make")));
                            spinner2.setSelection(Utilities.getSpinnerIndex(spinner2, vinqueryInfo.get("Model")));
                            ((EditText) findViewById(R.id.year)).setText(vinqueryInfo.get("Year"));
                        }
                        ocrPhotoCompleted = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    carPicFileName = "";
                    break;
            }
        }else{
            // result is not ok
            carPicFileName = "";
            Log.d("cancel", "return code is not ok");
        }
    }




}
