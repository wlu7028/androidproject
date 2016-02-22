package com.logbook.logbookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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



    public VehicleListAdapter(Context context, List<String> values,List<String> values2) {
        super(context, R.layout.display_vehicle_list, values);
        this.context = context;
        this.values = values;
        this.values2 = values2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.display_vehicle_list, parent, false);
        TextView carMakerAndModel = (TextView) rowView.findViewById(R.id.carmakermodel);
        TextView milesAndServiceDate = (TextView) rowView.findViewById(R.id.milesandservicedate);
        ImageView carThumbnail = (ImageView) rowView.findViewById(R.id.carthumbnail);
        carMakerAndModel.setText(values.get(position));
        milesAndServiceDate.setText(values2.get(position));
        return rowView;
    }
}
