package com.example.mzmey.myapplication;

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
}
