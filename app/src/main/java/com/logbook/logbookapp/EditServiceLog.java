package com.logbook.logbookapp;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class EditServiceLog extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText    datePicker,serviceCategories,serviceOdometer,serviceCost,serviceTag;
    private TextView vehicleName;
    private DatePickerDialog datePickerDialog;
    private int rowPosition,serviceLogPosition;
    private String setCheckBox = "";
    private ServiceLogObject tempServiceLogObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AppConstant.AppEnum.APPTITLE.getText());
        setSupportActionBar(toolbar);
        rowPosition = getIntent().getExtras().getInt("rowPosition");
        serviceLogPosition = getIntent().getExtras().getInt("serviceLogPosition");
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePicker = (EditText) findViewById(R.id.editServiceDatePicker);
        datePicker.setInputType(InputType.TYPE_NULL);
        datePicker.requestFocus();
        setDatePickerDialog();
        serviceCategories = (EditText) findViewById(R.id.editServiceCategories);
        serviceCategories.setInputType(InputType.TYPE_NULL);
        serviceCategories.requestFocus();
        setValuesToFields();

    }

    private void setValuesToFields(){
        tempServiceLogObj = ReadSaveDataUtility.vehicleObjects.get(rowPosition).getServiceLogObjects().get(serviceLogPosition);
        vehicleName = (TextView) findViewById(R.id.editServiceVehicleName);
        serviceOdometer = (EditText) findViewById(R.id.editServiceOdometer);
        serviceCost = (EditText) findViewById(R.id.editServiceCost);
        serviceTag = (EditText) findViewById(R.id.editServiceTag);
        datePicker.setText(tempServiceLogObj.getServiceDate());
        serviceCategories.setText(tempServiceLogObj.getServiceCategories());
        setCheckBox = tempServiceLogObj.getServiceCategories();
        vehicleName.setText(tempServiceLogObj.getVehicle());
        serviceOdometer.setText(tempServiceLogObj.getServiceOdometer());
        serviceCost.setText(tempServiceLogObj.getCost());
        serviceTag.setText(tempServiceLogObj.getTag());

    }
    public void saveServiceLog(View view){
        tempServiceLogObj.setVehicle(vehicleName.getText().toString());
        tempServiceLogObj.setServiceDate(datePicker.getText().toString());
        tempServiceLogObj.setCost(serviceCost.getText().toString());
        tempServiceLogObj.setServiceOdometer(serviceOdometer.getText().toString());
        tempServiceLogObj.setTag(serviceTag.getText().toString());
        tempServiceLogObj.setServiceCategories(serviceCategories.getText().toString());
        tempServiceLogObj.setModifiedDateTime((System.currentTimeMillis() / 1000L));
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

    public void showServiceCategoriesSelection(View view){
        Intent startServiceCategoriesSelection = new Intent(this,ServiceCategories.class);
        startServiceCategoriesSelection.putExtra("setcheckbox", setCheckBox);
        startActivityForResult(startServiceCategoriesSelection, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                setCheckBox = data.getExtras().getString("setcheckbox");
                serviceCategories.setText(setCheckBox);
            }
        }
    }

}
