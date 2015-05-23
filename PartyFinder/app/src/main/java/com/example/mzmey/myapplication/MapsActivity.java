<<<<<<< HEAD
package com.example.mzmey.myapplication;;
=======
package com.example.mzmey.myapplication;
>>>>>>> c07b592a365f012a50bf42b974177bcf338f70a3

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

<<<<<<< HEAD
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
=======
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
>>>>>>> c07b592a365f012a50bf42b974177bcf338f70a3
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

<<<<<<< HEAD
public class MapsActivity extends FragmentActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    final String TAG = "myLogs";
    Marker marker;
    LatLng latlng;
    LocationManager lm;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
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

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

/*		map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude))
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
*/	}

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
                Log.d(TAG, "onMapClick: " + latLng.latitude + ","
                        + latLng.longitude);
                marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Привет!"));
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

        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
            }
        });
    }

    // =============================================================================================
=======

/**
 * Created by MZmey on 19.05.2015.
 */
public class MapsActivity extends FragmentActivity {


        SupportMapFragment mapFragment;
        GoogleMap map;
        final String TAG = "myLogs";
        Marker marker;
        LatLng latlng;
        LocationManager lm;
        Location location;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            map = mapFragment.getMap();
            if (map == null) {
                finish();
                return;
            }
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

            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

/*		map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude))
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
*/	}

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
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    Log.d(TAG, "onMapClick: " + latLng.latitude + ","
                            + latLng.longitude);
                    marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .title("Ïðèâåò!"));
                }
            });

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng latLng) {
                    Log.d(TAG, "onMapLongClick: " + latLng.latitude + ","
                            + latLng.longitude);
                }
            });

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                public void onCameraChange(CameraPosition camera) {
                    Log.d(TAG, "onCameraChange: " + camera.target.latitude + ","
                            + camera.target.longitude);
                }
            });

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    marker.remove();
                }
            });
        }

        // =============================================================================================
>>>>>>> c07b592a365f012a50bf42b974177bcf338f70a3

/*		public void onClickMoving(View view) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(-27, 133)).zoom(5).bearing(45).tilt(20)
				.build();
		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newCameraPosition(cameraPosition);
		map.animateCamera(cameraUpdate);
	}
*/

<<<<<<< HEAD
    public void onClickSaveLocation(View view){
        double latitude = location.getLatitude();
        double longtitude  = location.getLongitude();
        latlng = new LatLng(latitude, longtitude);
        Toast.makeText(this, "Your coordinates were saved",
                Toast.LENGTH_SHORT).show();
    }
}
=======
        public void onClickSaveLocation(View view){
            double latitude = location.getLatitude();
            double longtitude  = location.getLongitude();
            latlng = new LatLng(latitude, longtitude);
            Toast.makeText(this, "Your coordinates were saved",
                    Toast.LENGTH_SHORT).show();
        }
    }
>>>>>>> c07b592a365f012a50bf42b974177bcf338f70a3
