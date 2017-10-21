package com.example.jeevan.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeevan on 28/2/17.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private HttpURLConnection connection;
    private InputStream inputStream;

    public String makeServiceCall(String reqUrl) {
        System.out.println(reqUrl);

        String response = null;
        try {
            URL url = new URL(reqUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            inputStream = new BufferedInputStream(connection.getInputStream());
            response = getDataFromStream(inputStream);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public Bitmap makeBitmapServiceCall(String reqUrl) {

        Bitmap b = null;
        try {

            URL imgUrl = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = new BufferedInputStream(connection.getInputStream());

            b = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    private String getDataFromStream(InputStream inputStream) {

        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = bufferReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
