package com.nordscript.chefy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Setup on 23/12/2015.
 */
public class OrdersAdapter extends ArrayAdapter<Order> {

    OrdersAdapter(Context context, int layout, ArrayList<Order> array) {
        super(context, layout, array);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.order_list_view, parent, false);
        }
        // Lookup view for data population
        TextView startTime = (TextView) convertView.findViewById(R.id.start_time);
        TextView orderItems = (TextView) convertView.findViewById(R.id.order_items);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.is_completed);

        startTime.setText(order.getStartTime().toString());
        orderItems.setText(order.getContactNumber());
        checkBox.setChecked(order.getCompleted());

        return convertView;
    }
}
