package com.example.jeevan.weatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedForcast extends AppCompatActivity {

    String condition, temperature, location, tempMax, tempMin, description;
    TextView  temperatureTextView, locationTextView, tempHighLowTextView, descriptionTextView;
    ImageView iconImage;
    Bitmap icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_forcast);
        Intent intent = getIntent();

        condition = intent.getExtras().getString("condition");
        temperature = intent.getExtras().getString("temperature");
        location = intent.getExtras().getString("location");
        icon = intent.getParcelableExtra("icon");
        description = intent.getExtras().getString("description");
        tempMax = intent.getExtras().getString("tempMax");
        tempMin = intent.getExtras().getString("tempMin");
        description= "Description: " + description;

        temperatureTextView = (TextView) findViewById(R.id.temperatureDetailed);
        locationTextView = (TextView) findViewById(R.id.locationDetailed);
        iconImage = (ImageView) findViewById(R.id.detailedImageView);
        tempHighLowTextView = (TextView) findViewById(R.id.tempHighLow);
        descriptionTextView = (TextView) findViewById(R.id.tempDescription);

        temperatureTextView.setText(temperature);
        locationTextView.setText(location);
        iconImage.setImageBitmap(icon);
        tempHighLowTextView.setText(tempMax +"\u00b0" + "C"+ "/" + tempMin+"\u00b0" + "C");
        descriptionTextView.setText(description);

    }

}
