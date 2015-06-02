package com.example.mzmey.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.*;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    final String TAG = "myLogs";
    private static final String CHECK_IN = "checkIn";
    private static final String URI = "uri";
    private final String LOGIN = "login";
    private static final String EVENT_NAME = "eventName";
    Marker marker;
    LatLng latlng;
    LocationManager lm;
    Location location;
    private String stPath;
    private static String URI_ADD = "/event";
    private String login;
    private RequestQueue queue;
    private String name;
    double latitude;
    double longitude;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        queue = MyQueue.getInstance(getApplicationContext()).getQueue();

        name = getIntent().getStringExtra(EVENT_NAME);
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
        latitude = 0;
        longitude = 0;
        init();

        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your device",
                    Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    // ============================================================================================
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(
                        "GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    // ============================================================================================

    private void init() {
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                Log.d(TAG, "onMapClick: " + latLng.latitude + ","
                        + latLng.longitude);
                marker = map.addMarker(new MarkerOptions().position(new LatLng(
                        latLng.latitude, latLng.longitude)));
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });

        map.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(TAG, "onMapLongClick: " + latLng.latitude + ","
                        + latLng.longitude);
            }
        });

        map.setOnCameraChangeListener(new OnCameraChangeListener() {

            public void onCameraChange(CameraPosition camera) {
                Log.d(TAG, "onCameraChange: " + camera.target.latitude + ","
                        + camera.target.longitude);
            }
        });
    }

    // =============================================================================================

    public void onClickSaveLocation(View view) {

        if ((longitude == 0) || (latitude == 0))
            Toast.makeText(this, "Отметьте местоположение на карте!", Toast.LENGTH_SHORT)
                    .show();
        else {

            String uri = stPath + URI_ADD + login;
            StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        Toast.makeText(getApplicationContext(), "Ваши координаты сохранены!", Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(getApplicationContext(), EventOld.class);
                        intent.putExtra(LOGIN, login);
                        intent.putExtra(URI, stPath);
                        intent.putExtra(EVENT_NAME, name);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Соединение разорвано", Toast.LENGTH_SHORT)
                            .show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(EVENT_NAME, name);
                    params.put(LOGIN, login);
                    params.put(CHECK_IN, "YES");
                    params.put("width", latitude + "");
                    params.put("height", longitude + "");
                    Log.d(TAG, "params sending are " + latitude + ", " + longitude);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            queue.add(sr);


        }
    }
}