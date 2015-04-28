package ru.startandroid.develop.p1391googlemaps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity {

  SupportMapFragment mapFragment;
  GoogleMap map;
  final String TAG = "myLogs";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    map = mapFragment.getMap();
    if (map == null) {
      finish();
      return;
    }
    init();
  }

  private void init() {
      map.setOnMapClickListener(new OnMapClickListener() {
      
      @Override
      public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);
      }
    });
      
      map.setOnMapLongClickListener(new OnMapLongClickListener() {
      
      @Override
      public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
      }
    });
      
      map.setOnCameraChangeListener(new OnCameraChangeListener() {
      
      @Override
      public void onCameraChange(CameraPosition camera) {
        Log.d(TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
      }
    });
    }


  public void onClickTest(View view) {
    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
  }
}