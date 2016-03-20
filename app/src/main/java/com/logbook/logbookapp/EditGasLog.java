package com.logbook.logbookapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class EditGasLog extends AppCompatActivity {
    private int rowPosition;
    private int gasLogPosition;
    private SimpleDateFormat dateFormatter;
    private EditText datePicker,gasLocation,gasOdometer,gasGallons,gasCost;
    private TextView vehicleName;
    private DatePickerDialog datePickerDialog;
    private GasLogObject tempGasLogObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gas_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("LogBook");
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        gasLogPosition = getIntent().getExtras().getInt("gasLogPosition");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePicker = (EditText) findViewById(R.id.editgaslogdatepicker);
        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.requestFocus();
        setDatePickerDialog();
        setValuesToFields();
    }

    private void setValuesToFields(){
        tempGasLogObject = ReadSaveDataUtility.vehicleObjects.get(rowPosition).getGasLogObjects().get(gasLogPosition);
        vehicleName = (TextView) findViewById(R.id.editgaslogvehiclename);
        gasLocation = (EditText) findViewById(R.id.editgasloglocation);
        gasOdometer = (EditText) findViewById(R.id.editgaslogodometers);
        gasGallons = (EditText) findViewById(R.id.editgasloggallon);
        gasCost = (EditText) findViewById(R.id.editgaslogtotalcost);
        datePicker.setText(tempGasLogObject.getAddGasDate());
        vehicleName.setText(tempGasLogObject.getVehicle());
        gasLocation.setText(tempGasLogObject.getLocation());
        gasOdometer.setText(tempGasLogObject.getGasOdometer());
        gasGallons.setText(tempGasLogObject.getGallon());
        gasCost.setText(tempGasLogObject.getTotalCost());

    }

    public void saveGasLog(View view){
        tempGasLogObject.setVehicle(vehicleName.getText().toString());
        tempGasLogObject.setAddGasDate(datePicker.getText().toString());
        tempGasLogObject.setGallon(gasGallons.getText().toString());
        tempGasLogObject.setGasOdometer(gasOdometer.getText().toString());
        tempGasLogObject.setTotalCost(gasCost.getText().toString());
        tempGasLogObject.setLocation(gasLocation.getText().toString());
        tempGasLogObject.setModifiedDateTime((System.currentTimeMillis() / 1000L));
        ReadSaveDataUtility.saveVehicleListToSharedPreference(this);
        finish();
    }

    public void cancelButton(View view){
        finish();
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

}
