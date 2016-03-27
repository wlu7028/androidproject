package com.logbook.logbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


/**
 * Created on 2/21/2016.
 */
public class VehicleListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<String> values2;
    private final List<String> vehicleIcons;



    public VehicleListAdapter(Context context, List<String> values,List<String> values2,List<String> vehicleIcons) {
        super(context, R.layout.display_vehicle_list, values);
        this.context = context;
        this.values = values;
        this.values2 = values2;
        this.vehicleIcons = vehicleIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.display_vehicle_list, parent, false);
        TextView carMakerAndModel = (TextView) rowView.findViewById(R.id.carmakermodel);
        TextView milesAndServiceDate = (TextView) rowView.findViewById(R.id.milesandservicedate);
        ImageView carThumbnail = (ImageView) rowView.findViewById(R.id.carthumbnail);
        ImageButton deleteVehicleButton = (ImageButton) rowView.findViewById(R.id.deletevehicle);
        deleteVehicleButton.setFocusable(false);
        deleteVehicleButton.setTag(position);
        carMakerAndModel.setText(values.get(position));
        milesAndServiceDate.setText(values2.get(position));
        if(!vehicleIcons.get(position).isEmpty())
            carThumbnail.setImageBitmap(ReadSaveDataUtility.loadBitmapFromInternalStorage(getContext(),vehicleIcons.get(position)));
        deleteVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int deletePosition = (Integer)view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Warning: delete this vehicle permanently");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ReadSaveDataUtility.deleteVehicle((Activity) context, deletePosition);
                                values.remove(deletePosition);
                                values2.remove(deletePosition);
                                VehicleListAdapter.this.notifyDataSetChanged();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = builder.create();
                alert1.show();
            }
        });
        return rowView;
    }


}
