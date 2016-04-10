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



public class AddAVehicle extends AppCompatActivity  {

    Spinner spinner1,spinner2,spinner3;
    String carPicFileName = "";
    ImageButton carPicButton;
    Bitmap changeVehicleIcon= null;
    private ProgressDialog pd;
    private Button getOCRButton;
    private String ocrResult,ocrTempFileLocation;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3;
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

        spinner3 = (Spinner) findViewById(R.id.trim);
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
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        try {
                            getOCRPhoto();
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (pd!=null) {
                            pd.dismiss();
                            getOCRButton.setEnabled(true);
                        }
                    }

                };
                task.execute((Void[])null);
            }
        });

    }

    public void saveVehicle(View view){
        //SharedPreferences sp = this.getSharedPreferences("CarDataSharedPref",Context.MODE_PRIVATE);
       // List<String> carDataSP = ReadSaveDataUtility.readSharedPreference(this);
        CarObject carObj = new CarObject();
        carObj.setCarMaker(((Spinner) findViewById(R.id.maker)).getSelectedItem().toString());
        carObj.setCarModel(((Spinner) findViewById(R.id.model)).getSelectedItem().toString());
        carObj.setCarTrim(((Spinner) findViewById(R.id.trim)).getSelectedItem().toString());
        carObj.setCarYear(((EditText) findViewById(R.id.year)).getText().toString());
        carObj.setCarLicensePlateNumber(((EditText) findViewById(R.id.licenseplatenumber)).getText().toString());
        carObj.setCarVIN(((EditText) findViewById(R.id.vin)).getText().toString());
        carObj.setCarOdometer(((EditText) findViewById(R.id.odometer)).getText().toString());
        carObj.setCarPicFileLocation(carPicFileName);
        carObj.setCreatedTimeStamp(System.currentTimeMillis() / 1000L);
        if(changeVehicleIcon != null && !carPicFileName.isEmpty())
            ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), changeVehicleIcon, carPicFileName);
        ReadSaveDataUtility.vehicleObjects.add(carObj);

        /*
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("CarData_size", carDataSP.size());
        for(int i=0;i<carDataSP.size();i++)
        {
            mEdit1.remove("CarData_" + i);
            mEdit1.putString("CarData_" + i, carDataSP.get(i));
        }
        mEdit1.commit();
        */
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    /*
    private List<String> readSharedPreference(){
        SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
        List<String> carDataSP = new ArrayList<>();
        int size = sp.getInt("CarData_size", 0);
        for(int i=0;i<size;i++)
        {
            if(sp.getString("CarData_" + i,null) != null)
                carDataSP.add(sp.getString("CarData_" + i, null));
        }
        return carDataSP;
    }*/

    public void cancelButton(View view){
        finish();
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = CameraControl.createImageFile(this);
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
                        File photoFile = CameraControl.createTempOCRFile(this);
                        ocrTempFileLocation = photoFile.getAbsolutePath();
                        ReadSaveDataUtility.saveBitmapToInternalStorage(getBaseContext(), (Bitmap) extras.get("data"), photoFile.getName());
                        ocrResult = RestServiceUtility.processOCR(ocrTempFileLocation);
                        ((EditText) findViewById(R.id.vin)).setText(ocrResult);
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
