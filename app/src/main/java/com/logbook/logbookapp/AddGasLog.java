package com.logbook.logbookapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.logbook.logbookapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddGasLog extends AppCompatActivity {
    private SimpleDateFormat dateFormatter;
    private EditText datePicker;
    private DatePickerDialog datePickerDialog;
    private int rowPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gas_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LogBook");
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePicker = (EditText) findViewById(R.id.addgaslogdatepicker);
        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.requestFocus();
        setDateTimeField();
        TextView vehicleName = (TextView) findViewById(R.id.addgaslogvehiclename);
        StringBuilder strBuilder = new StringBuilder(100);
        strBuilder.append(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarMaker());
        strBuilder.append(" ");
        strBuilder.append(ReadSaveDataUtility.vehicleObjects.get(rowPosition).getCarModel());
        vehicleName.setText(strBuilder.toString());
        TextInputLayout odometerText = (TextInputLayout) findViewById(R.id.addgaslogodometersTextInputLayout);
        //odometerText.setErrorEnabled(true);
        //odometerText.setError("Required");

    }

    public void saveGasLog(View view){
        GasLogObject tempGasLogObject = new GasLogObject();
        tempGasLogObject.setVehicle(((TextView) findViewById(R.id.addgaslogvehiclename)).getText().toString());
        tempGasLogObject.setAddGasDate(((EditText) findViewById(R.id.addgaslogdatepicker)).getText().toString());
        tempGasLogObject.setGallon(((EditText) findViewById(R.id.addgasloggallon)).getText().toString());
        tempGasLogObject.setGasOdometer(((EditText) findViewById(R.id.addgaslogodometers)).getText().toString());
        tempGasLogObject.setTotalCost(((EditText) findViewById(R.id.addgaslogtotalcost)).getText().toString());
        tempGasLogObject.setLocation(((EditText) findViewById(R.id.addgasloglocation)).getText().toString());
        tempGasLogObject.setUserEntryDateTime((System.currentTimeMillis() / 1000L));
        ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().add(tempGasLogObject);
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    public void cancelButton(View view){
        finish();
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
}
