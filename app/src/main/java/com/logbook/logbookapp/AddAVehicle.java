package com.logbook.logbookapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;


public class AddAVehicle extends AppCompatActivity  {

    Spinner spinner1,spinner2,spinner3;
    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_avehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                switch(carModel){
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

    }

    public void saveVehicle(View view){
        SharedPreferences sp = this.getSharedPreferences("CarDataSharedPref",Context.MODE_PRIVATE);
        List<String> carDataSP = ReadSaveDataUtility.readSharedPreference(this);
        CarObject carObj = new CarObject();
        carObj.setCarMaker(((Spinner) findViewById(R.id.maker)).getSelectedItem().toString());
        carObj.setCarModel(((Spinner) findViewById(R.id.model)).getSelectedItem().toString());
        carObj.setCarTrim(((Spinner) findViewById(R.id.trim)).getSelectedItem().toString());
        carObj.setCarYear(((EditText) findViewById(R.id.year)).getText().toString());
        carObj.setCarLicensePlateNumber(((EditText) findViewById(R.id.licenseplatenumber)).getText().toString());
        carObj.setCarVIN(((EditText) findViewById(R.id.vin)).getText().toString());
        carObj.setCarOdometer(((EditText) findViewById(R.id.odometer)).getText().toString());
        carObj.setCreatedTimeStamp(System.currentTimeMillis() / 1000L);
        carDataSP.add(carObj.toString());
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putInt("CarData_size", carDataSP.size());

        for(int i=0;i<carDataSP.size();i++)
        {
            mEdit1.remove("CarData_" + i);
            mEdit1.putString("CarData_" + i, carDataSP.get(i));
        }
        mEdit1.commit();
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

}
