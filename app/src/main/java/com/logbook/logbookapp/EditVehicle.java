package com.logbook.logbookapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;


public class EditVehicle extends AppCompatActivity {
    Spinner spinner1,spinner2,spinner3;
    ImageButton carPicButton;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3;
    int rowPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");

        carPicButton = (ImageButton) findViewById(R.id.editVehicleIconButton);
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
        EditText editVin = (EditText) findViewById(R.id.editvin);
        editVin.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarVIN());
        EditText editOdometer = (EditText) findViewById(R.id.editodometer);
        editOdometer.setText(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarOdometer());

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
            File photoFile = null;
            try {
                photoFile = CameraControl.createImageFile(this);
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CameraControl.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraControl.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            carPicButton.setImageBitmap(imageBitmap);
            carPicButton.invalidate();
        }
    }
}
