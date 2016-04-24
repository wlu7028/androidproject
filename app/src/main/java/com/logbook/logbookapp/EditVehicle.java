package com.logbook.logbookapp;


import android.app.ProgressDialog;
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


public class EditVehicle extends AppCompatActivity {
    Spinner spinner1,spinner2,spinner3;
    ImageButton carPicButton;
    EditText editVin;
    Bitmap changeVehicleIcon= null;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3;
    int rowPosition;
    private ProgressDialog pd;
    private  Button getOCRButton;
    private boolean ocrPhotoCompleted = false;
    private String ocrResult,ocrTempFileLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");

        carPicButton = (ImageButton) findViewById(R.id.editVehicleIconButton);
        editVin = (EditText) findViewById(R.id.editvin);

        if(!ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation().isEmpty()){
            carPicButton.setImageBitmap(ReadSaveDataUtility.loadBitmapFromInternalStorage(getBaseContext(),
                    ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarPicFileLocation() ));
        }
        spinner1 = (Spinner) findViewById(R.id.editmaker);
        adapter1 = ArrayAdapter.createFromResource(this,
                R.array.car_maker, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(getIndex(spinner1, ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarMaker()));

        spinner2 = (Spinner) findViewById(R.id.editmodel);
        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.car_model, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(getIndex(spinner2, ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarModel()));

        spinner3 = (Spinner) findViewById(R.id.edittrim);
        adapter3 = ArrayAdapter.createFromResource(this,
                R.array.car_trim, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                String carModel = parentView.getItemAtPosition(position).toString();
                switch (carModel) {
                    case "Honda":
                        adapter2 = ArrayAdapter.createFromResource(EditVehicle.this,
                                R.array.honda_model, android.R.layout.simple_spinner_item);
                        break;
                    case "Toyota":
                        adapter2 = ArrayAdapter.createFromResource(EditVehicle.this,
                                R.array.toyota_model, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        adapter2 = ArrayAdapter.createFromResource(EditVehicle.this,
                                R.array.car_model, android.R.layout.simple_spinner_item);
                        break;
                }

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
        getOCRButton = (Button) findViewById(R.id.getOCREditV);
        getOCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

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
                        try {
                            while (!ocrPhotoCompleted) {
                                Thread.sleep(2000);
                            }
                            ocrResult = RestServiceUtility.processOCR(ocrTempFileLocation);
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
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
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).setCarTrim(((Spinner) findViewById(R.id.edittrim)).getSelectedItem().toString());
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
                File photoFile = CameraControl.createImageFile(this);
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
                        File photoFile = CameraControl.createTempOCRFile(this);
                        ocrTempFileLocation = photoFile.getAbsolutePath();
                        ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), (Bitmap) extras.get("data"), photoFile.getName());
                        ocrPhotoCompleted = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }else{
            // result is not ok
            Log.d("cancel", "return code is not ok");
        }
    }
}
