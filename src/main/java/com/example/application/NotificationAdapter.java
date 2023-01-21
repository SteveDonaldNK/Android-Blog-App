package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {
    private ArrayList<NotificationModel> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView from;
        TextView time;
    }

    public NotificationAdapter(ArrayList<NotificationModel> data, Context context) {
        super(context, R.layout.notification_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPos = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationModel notificationModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_layout, parent, false);
            viewHolder.from = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.from.setText(notificationModel.getFrom());
        viewHolder.time.setText(notificationModel.getTime());

        return convertView;
    }
}
