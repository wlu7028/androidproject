package com.logbook.logbookapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 3/14/2016.
 */
public class ServiceCategoryAdapter extends ArrayAdapter<ServiceCategoryObject> {
    private final List<ServiceCategoryObject> serviceCategoryList;
    private final Activity context;

    public ServiceCategoryAdapter(Activity context, List<ServiceCategoryObject> list) {
        super(context, R.layout.display_servicecategories_list, list);
        this.context = context;
        this.serviceCategoryList = list;
    }
    static class ViewHolder {
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.display_servicecategories_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.servicecategorycheckbox);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            ServiceCategoryObject element = (ServiceCategoryObject) viewHolder.checkbox
                                    .getTag();
                            element.setSelected(buttonView.isChecked());

                        }
                    });
            viewHolder.checkbox.setText(serviceCategoryList.get(position).getName());
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(serviceCategoryList.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(serviceCategoryList.get(position));
            ((ViewHolder) view.getTag()).checkbox.setText(serviceCategoryList.get(position).getName());
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.checkbox.setChecked(serviceCategoryList.get(position).isSelected());
        return view;
    }
}
