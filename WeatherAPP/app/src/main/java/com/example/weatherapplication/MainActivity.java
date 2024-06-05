package com.example.weatherapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    private Button getWeather;

    private TextView time;

    private TextView city;
    private TextView dd;

    private TextView details;
    private LocationRequest locationRequest;

    DecimalFormat df = new DecimalFormat("#.##");

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "7367cf897efa86febd46ba77e22443b5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWeather = findViewById(R.id.showWeather);
        dd = findViewById(R.id.DD);
        time = findViewById(R.id.time);
        city = findViewById(R.id.address);
        details = findViewById(R.id.details);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (isGPSEnabled()) {
                            LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        dd.setText("Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                                        getDateAndTime();
                                        getWeatherDetails(latitude, longitude);
                                    }
                                }
                            }, Looper.getMainLooper());
                        } else {
                            Toast.makeText(MainActivity.this, "Please Turn On the Location", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    public void getDateAndTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
        time.setText("Date: " + formattedDate + "\nTime: " + formattedTime);

    }

    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String add = "";
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                    add += addresses.get(0).getAddressLine(i) + "\n";
            }
            city.setText(add);
            Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            city.setText("Error: " + e.getMessage());
        }
    }

    private void getWeatherDetails(double latitude, double longitude) {
        String tempUrl = url + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("response", s);  // Log the full response

                String Details = "";

                try {
                    JSONObject jsonResponse = new JSONObject(s);

                    // Log the entire response to see its structure
                    Log.d("JSON Response", jsonResponse.toString());

                    if (jsonResponse.has("weather") && jsonResponse.has("main") && jsonResponse.has("wind") &&
                            jsonResponse.has("clouds") && jsonResponse.has("sys") && jsonResponse.has("name")) {

                        // Parse the weather array
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");

                        // Parse the main object
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");

                        // Parse the wind object
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");

                        // Parse the clouds object
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String cloud = jsonObjectClouds.getString("all");

                        // Parse the sys object
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String country = jsonObjectSys.getString("country");

                        // Get the city name
                        String cityName = jsonResponse.getString("name");

                        city.setText(cityName);

                        Details += "Current weather of " + cityName + " (" + country + ")"
                                + "\n Temp: " + df.format(temp) + " °C"
                                + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind Speed: " + wind + " m/s (meters per second)"
                                + "\n Cloudiness: " + cloud + "%"
                                + "\n Pressure: " + pressure + " hPa";

                        details.setText(Details);
                    } else {
                        // Handle missing keys in the JSON response
                        Toast.makeText(MainActivity.this, "Incomplete weather data received", Toast.LENGTH_SHORT).show();
                        Log.e("response", "Incomplete weather data received");
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "JSON ERROR: " + e, Toast.LENGTH_SHORT).show();
                    Log.e("response", "JSON error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
                Log.e("response", "Volley error", volleyError);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}