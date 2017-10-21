package com.example.jeevan.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class SettingsActivity extends AppCompatActivity {
    TextView locationTV, selectDaysTV;
    String locationName;
    Spinner spinner;
    Button saveButton;
    String longitude;
    String latitude;
    String days;
    String TAG = this.getClass().getSimpleName();
    PlaceAutocompleteFragment autocompleteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // locationET = (EditText) findViewById(R.id.location_editText);
        locationTV = (TextView) findViewById(R.id.location_textView);
        selectDaysTV = (TextView) findViewById(R.id.selectDateTV);
        spinner = (Spinner) findViewById(R.id.spinner);
        saveButton = (Button) findViewById(R.id.save_button);

        CharSequence noOfDays[] = new CharSequence[]{"1", "2", "3", "4", "5", "6", "7"};

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, noOfDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                days = (String) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), days, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "NOTHING SELECTED", Toast.LENGTH_LONG).show();
            }
        });

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng query = place.getLatLng();

                longitude = String.valueOf(query.longitude);
                latitude = String.valueOf(query.latitude);
                locationName = String.valueOf(place.getName());
                System.out.println(locationName);


            }

            @Override
            public void onError(Status status) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), locationName, Toast.LENGTH_LONG).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("longitude", longitude);
                editor.putString("latitude", latitude);
                editor.putString("days", days);
                editor.putString("location", locationName);
                editor.apply();

            }
        });

    }


}

