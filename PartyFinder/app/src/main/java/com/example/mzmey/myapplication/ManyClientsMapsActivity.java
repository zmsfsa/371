package com.example.mzmey.myapplication;

<<<<<<< HEAD
/**
 * Created by Федор on 23.05.2015.
 */

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
import android.widget.Toast;

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

public class ManyClientsMapsActivity extends FragmentActivity {

    LatLng clients_latlng;
    SupportMapFragment mapFragment;
    GoogleMap map;
    double latitude;
    double longtitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_clients_activity);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
        latitude = 55.45;
        longtitude = 37.36;
        init();

    }

    public void init() {
        // for (int i = 0; i < clients_latlng.length; i++) {
        map.addMarker(new MarkerOptions().position(
                new LatLng(latitude, longtitude)).icon(
                BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // }
    }
=======
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by MZmey on 19.05.2015.
 */
public class ManyClientsMapsActivity extends FragmentActivity {

        LatLng[] clients_latlng;
        SupportMapFragment mapFragment;
        GoogleMap map;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.many_clients_activity);
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            map = mapFragment.getMap();
            if (map == null) {
                finish();
                return;
            }
            init();
        }

        public void init() {
            for (int i = 0; i < clients_latlng.length; i++) {
                map.addMarker(new MarkerOptions().position(clients_latlng[i]).icon(
                        BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }
>>>>>>> c07b592a365f012a50bf42b974177bcf338f70a3
}
