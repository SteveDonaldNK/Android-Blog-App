package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class saveAdapter extends ArrayAdapter<saveModel> {
    private ArrayList<saveModel> dataSet;
    private Context mContext;

    private static class ViewHolder {
        TextView userName;
        TextView publishTime;
        TextView readTime;
        TextView postContent;
    }

    public saveAdapter(ArrayList<saveModel> data, Context context) {
        super(context, R.layout.save_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPos = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        saveModel sm = getItem(position);
        saveAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new saveAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.save_layout, parent, false);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.publishTime = (TextView) convertView.findViewById(R.id.publishTime);
            viewHolder.readTime = (TextView) convertView.findViewById(R.id.readTime);
            viewHolder.postContent = (TextView) convertView.findViewById(R.id.postContent);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (saveAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.userName.setText(sm.getUserName());
        viewHolder.publishTime.setText(sm.getPublishTime());
        viewHolder.postContent.setText(sm.getPostContent());
        viewHolder.readTime.setText(sm.getReadTime());

        return convertView;
    }
}
