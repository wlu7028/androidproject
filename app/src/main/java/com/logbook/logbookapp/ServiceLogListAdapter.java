package com.logbook.logbookapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 3/12/2016.
 */
public class ServiceLogListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<String> values2;
    private final int rowPosition;

    public ServiceLogListAdapter(Context context, List<String> values,List<String> values2,int rowPosition) {
        super(context, R.layout.display_servicelog_list, values);
        this.context = context;
        this.values = values;
        this.values2 = values2;
        this.rowPosition = rowPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.display_servicelog_list, parent, false);
        TextView serviceDateAndCategories = (TextView) rowView.findViewById(R.id.servicedateandcategories);
        TextView serviceOdometers = (TextView) rowView.findViewById(R.id.serviceodometers);
        ImageView carThumbnail = (ImageView) rowView.findViewById(R.id.carthumbnail);
        ImageButton deleteServiceLogButton = (ImageButton) rowView.findViewById(R.id.deleteservicelog);
        deleteServiceLogButton.setFocusable(false);
        deleteServiceLogButton.setTag(position);
        serviceDateAndCategories.setText(values.get(position));
        serviceOdometers.setText(values2.get(position));
        deleteServiceLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int deletePosition = (Integer)view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Warning: delete this service log permanently");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ReadSaveDataUtility.deleteServiceLog((Activity) context, deletePosition,rowPosition);
                                values.remove(deletePosition);
                                values2.remove(deletePosition);
                                ServiceLogListAdapter.this.notifyDataSetChanged();
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
