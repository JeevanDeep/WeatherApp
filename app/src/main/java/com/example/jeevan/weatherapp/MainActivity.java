package com.example.jeevan.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String imageUrl =
            "http://openweathermap.org/img/w/";
    private static String urlStart =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static String urlEnd = "&units=metric";
    private static String DAYS = "7";
    private static String APIKEY = "767abc659df45a86a765d1d8df335933";
    private static String MODE = "metric";
    private Intent intent;
    private ListView listView;
    private ArrayList<HashMap<String, String>> weatherData;
    private ArrayList<Bitmap> arrayList;
    private Bitmap conditionImage;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JsonAdapter adapter;
    private HashMap<String, String> parsedJsonData;
    private ProgressDialog progressDialog;
    private IntentFilter filter;
    private LocalBroadcastManager broadcastManager;
    private String latitude;
    private String longitude;
    private String locationName;

    //    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            longitude = intent.getStringExtra("longitude");
//            latitude = intent.getStringExtra("latitude");
//
////            Log.e("CITY IS ", message);
//
//            GetData getData = new GetData();
//            getData.execute();
//
//        }
//    };
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        listView = (ListView) findViewById(R.id.listViewOfData);
        weatherData = new ArrayList<>();
        arrayList = new ArrayList<>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> item = adapter.getItem(position);

                String location = null;
                if (item != null) {
                    location = item.get("location");
                }
                String temperature = null;
                if (item != null) {
                    temperature = item.get("temp");
                }
                String condition = null;
                if (item != null) {
                    condition = item.get("condition");
                }
                String tempMax = null;
                if (item != null) {
                    tempMax = item.get("tempMax");
                }
                String tempMin = null;
                if (item != null) {
                    tempMin = item.get("tempMin");
                }
                String description = null;
                if (item != null) {
                    description = item.get("description");
                }

                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap iconCondition = drawable.getBitmap();

                Intent callDetailedForcast = new Intent(MainActivity.this, DetailedForcast.class);

                callDetailedForcast.putExtra("location", location);
                callDetailedForcast.putExtra("temperature", temperature);
                callDetailedForcast.putExtra("condition", condition);
                callDetailedForcast.putExtra("icon", iconCondition);
                callDetailedForcast.putExtra("tempMax", tempMax);
                callDetailedForcast.putExtra("tempMin", tempMin);
                callDetailedForcast.putExtra("description", description);

                startActivity(callDetailedForcast);

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                GetData getData = new GetData();
                if (adapter != null) {
                    weatherData.clear();
                    adapter.notifyDataSetChanged();
                }
                getData.execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
//                    Log.i("LONGG bro", longitude);
//                    Log.i("LATTT bro", latitude);
                    GetData getData = new GetData();
                    if (adapter != null) {
                        weatherData.clear();
                        adapter.notifyDataSetChanged();
                    }
                    getData.execute();

                } else {
                    Log.i(TAG, "onLocationChanged: FALSE");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Toast.makeText(MainActivity.this, "Please enable your GPS", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.syncNowMenu:
                GetData jsonData = new GetData();
                weatherData.clear();
                getSettingsData();
                jsonData.execute();
                break;
            case R.id.getLocationMenu:
                Intent intent = new Intent(MainActivity.this, LocationFinder.class);
                startActivity(intent);
                break;
            case R.id.settingsMenu:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void getSettingsData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tempLong = preferences.getString("longitude", "");
        String tempLat = preferences.getString("latitude", "");
        locationName = preferences.getString("location", "");
        if (!tempLat.equals(tempLong)) {
            latitude = tempLat;
            longitude = tempLong;
        }
        DAYS = preferences.getString("days", "");
//        System.out.println("shared prefs data received as "+temp);
    }

    private String getDate(int i) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM");
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }


    private class GetData extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            if (!isFinishing()) {

                progressDialog.show();
            }


        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String jsonString;

            String url;
            String sb = urlStart +
                    "lat=" +
                    latitude +
                    "&lon=" +
                    longitude +
                    "&units=" +
                    MODE +
                    "&cnt=" +
                    DAYS +
                    "&appid=" +
                    APIKEY;

            url = sb;

            jsonString = handler.makeServiceCall(url);
            Log.e(TAG, "Response from url" + jsonString);
            if (jsonString != null) {

                try {


                    JSONObject jsonObject = new JSONObject(jsonString);


                    JSONArray list = jsonObject.getJSONArray("list");

                    for (int i = 0; i < list.length(); i++) {

                        String currentDate = getDate(i);

                        JSONObject c = list.getJSONObject(i);


                        JSONObject jsonObject1 = jsonObject.getJSONObject("city");
                        String location = jsonObject1.getString("name");

                        if (locationName != null) {
                            location = locationName;
                        }

                        JSONObject jsonObject2 = c.getJSONObject("temp");

                        double temp = jsonObject2.getDouble("day");
                        double tempMax = jsonObject2.getDouble("max");
                        double tempMin = jsonObject2.getDouble("min");


                        JSONArray jsonArray = c.getJSONArray("weather");

                        JSONObject object = jsonArray.getJSONObject(0);

                        String condition = object.getString("main");

                        String description = object.getString("description");

                        conditionImage = handler.makeBitmapServiceCall
                                (imageUrl + object.getString("icon") + ".png");

                        Log.v("ICON LINK", imageUrl + object.getString("icon") + ".png");


                        parsedJsonData = new HashMap<>();

                        arrayList.add(conditionImage);


                        parsedJsonData.put("condition", condition);
                        parsedJsonData.put("temp", String.valueOf(temp) + "\u00b0" + "C");
                        parsedJsonData.put("location", location);
                        parsedJsonData.put("date", currentDate);
                        parsedJsonData.put("description", description);
                        parsedJsonData.put("tempMax", String.valueOf(tempMax));
                        parsedJsonData.put("tempMin", String.valueOf(tempMin));
                        weatherData.add(parsedJsonData);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            adapter = new JsonAdapter(weatherData, arrayList, MainActivity.this);
            listView.setAdapter(adapter);
        }

    }
}
