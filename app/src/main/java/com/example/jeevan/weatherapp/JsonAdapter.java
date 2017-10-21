package com.example.jeevan.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeevan on 2/3/17.
 */

class JsonAdapter extends ArrayAdapter<HashMap<String, String>> {
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater;
    private ArrayList<Bitmap> arrayList;

    public JsonAdapter(ArrayList<HashMap<String, String>> data, ArrayList<Bitmap> arrayList, Context context) {
        super(context, 0);
        this.arrayList = arrayList;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_view_of_data, parent, false);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location);
            viewHolder.condition = (TextView) convertView.findViewById(R.id.condition);
            viewHolder.temp = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.conditionImage = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> data = this.data.get(position);

        viewHolder.location.setText(data.get("location"));
        viewHolder.condition.setText(data.get("condition"));
        viewHolder.temp.setText(data.get("temp"));
        viewHolder.date.setText(data.get("date"));
        viewHolder.conditionImage.setImageBitmap(arrayList.get(position));
        return convertView;
    }

    @Nullable
    @Override
    public HashMap<String, String> getItem(int position) {
        return data.get(position);
    }

    private static class ViewHolder {
        TextView date;
        TextView location;
        TextView temp;
        TextView condition;
        ImageView conditionImage;

    }

}
