package ru.startandroid.develop.p1391googlemaps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

	SupportMapFragment mapFragment;
	GoogleMap map;
	final String TAG = "myLogs";
	Marker marker;

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

	}

	private void init() {
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				Log.d(TAG, "onMapClick: " + latLng.latitude + ","
						+ latLng.longitude);
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

	public void onClickMarker(View view) {
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
				"Привет!"));
	}
	
	public void onClickMoving(View view){		
		  CameraPosition cameraPosition = new CameraPosition.Builder()
		  .target(new LatLng(-27, 133)) .zoom(5) .bearing(45) .tilt(20)
		  .build(); 
		  CameraUpdate cameraUpdate =
		  CameraUpdateFactory.newCameraPosition(cameraPosition);
		  map.animateCamera(cameraUpdate);
	}

}