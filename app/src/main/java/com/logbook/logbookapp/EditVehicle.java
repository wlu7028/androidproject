package com.logbook.logbookapp;


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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditVehicle extends AppCompatActivity {
    Spinner spinner1,spinner2;
    ImageButton carPicButton;
    EditText editVin;
    Bitmap changeVehicleIcon= null;
    ArrayAdapter<String> adapter1,adapter2;
    int rowPosition;
    private ProgressDialog pd;
    private  ImageButton getOCRButton;
    private boolean ocrPhotoCompleted = false;
    private String ocrResult,ocrTempFileLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        final Map<String, List<String>> vehicleMakerModelMap = Utilities.parseVehicleMakersModels(getResources().openRawResource(R.raw.vehicle_makers_models));

        rowPosition = getIntent().getExtras().getInt("rowPosition");

        carPicButton = (ImageButton) findViewById(R.id.editVehicleIconButton);
        editVin = (EditText) findViewById(R.id.editvin);

        if(!ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation().isEmpty()){
            carPicButton.setImageBitmap(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext(),
                    ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation() ));
        }
        spinner1 = (Spinner) findViewById(R.id.editmaker);
        adapter1 = adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                vehicleMakerModelMap.keySet().toArray(new String[vehicleMakerModelMap.keySet().size()]));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(getIndex(spinner1, ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarMaker()));

        spinner2 = (Spinner) findViewById(R.id.editmodel);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.car_model));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(getIndex(spinner2, ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarModel()));

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
        EditText editYear = (EditText) findViewById(R.id.edityear);
        editYear.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarYear());
        EditText editLicenseplateNumber = (EditText) findViewById(R.id.editlicenseplatenumber);
        editLicenseplateNumber.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarLicensePlateNumber());
        editVin.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarVIN());
        EditText editOdometer = (EditText) findViewById(R.id.editodometer);
        editOdometer.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarOdometer());


        getOCRButton = (ImageButton) findViewById(R.id.getOCREditV);
        getOCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    Map<String,String> vinqueryInfo = new HashMap<String, String>();
                    @Override
                    protected void onPreExecute() {
                        pd = new ProgressDialog(EditVehicle.this);
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
                                Log.d("ocrworker", "in while, tried=" + tried);
                                ocrResult = RestServiceUtility.googleMobileVisionOCRProcess(getBaseContext(),ocrTempFileLocation);
                                if(!ocrResult.isEmpty())
                                    vinqueryInfo = Utilities.processXmlResult(RestServiceUtility.processVIN(ocrResult));
                                else
                                    Log.d("ocrworker", "vin number is empty from ocr detection");
                                ocrPhotoCompleted = true;
                                Thread.sleep(2000);
                            }
                            ocrResult = RestServiceUtility.processOCR(ocrTempFileLocation);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        //update UI
                        ((EditText) findViewById(R.id.editvin)).setText(ocrResult);
                        if(!vinqueryInfo.isEmpty()){
                            spinner1.setSelection(Utilities.getSpinnerIndex(spinner1, vinqueryInfo.get("Make")));
                            spinner2.setSelection(Utilities.getSpinnerIndex(spinner2, vinqueryInfo.get("Model")));
                            ((EditText) findViewById(R.id.edityear)).setText(vinqueryInfo.get("Year"));
                        }
                        if (pd != null) {
                            pd.dismiss();
                            getOCRButton.setEnabled(true);
                        }
                    }

                };
                task.execute((Void[]) null);
                editVin.setText(ocrResult);
            }

        });


    }

    public void cancelButton(View view){
        finish();
    }

    public void saveVehicle(View view){
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarMaker(((Spinner) findViewById(R.id.editmaker)).getSelectedItem().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarModel(((Spinner) findViewById(R.id.editmodel)).getSelectedItem().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarYear(((EditText) findViewById(R.id.edityear)).getText().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarLicensePlateNumber(((EditText) findViewById(R.id.editlicenseplatenumber)).getText().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarVIN(((EditText) findViewById(R.id.editvin)).getText().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarOdometer(((EditText) findViewById(R.id.editodometer)).getText().toString());
        ReadSaveDataUtility.vehicleObjects.get(rowPosition) .setEditTimeStamp(System.currentTimeMillis() / 1000L);
        if(changeVehicleIcon != null && !ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation().isEmpty()){
            ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), changeVehicleIcon,
                    ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation());
        }else if (changeVehicleIcon != null){
            try {
                File photoFile = Utilities.createImageFile(this);
                String carPicFileName = "Real_"+photoFile.getName();
                ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarPicFileLocation(carPicFileName);
                ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), changeVehicleIcon,
                        carPicFileName);
            } catch (IOException ex) {
            }

        }
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void changePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, AppConstant.REQUEST_IMAGE_CAPTURE);
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
            switch(requestCode){
                case AppConstant.REQUEST_IMAGE_CAPTURE:
                    changeVehicleIcon = (Bitmap) extras.get("data");
                    carPicButton.setImageBitmap(changeVehicleIcon);
                    //carPicButton.invalidate();
                    break;
                case AppConstant.GET_OCR_File:
                    try {
                        File photoFile = Utilities.createTempOCRFile(this);
                        ocrTempFileLocation = photoFile.getAbsolutePath();
                        ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), (Bitmap) extras.get("data"), photoFile.getName());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }else{
            // result is not ok
            ocrPhotoCompleted = true;
            Log.d("cancel", "return code is not ok");
        }
    }
}
