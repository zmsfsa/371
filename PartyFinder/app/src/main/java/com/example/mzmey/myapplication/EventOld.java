package com.example.mzmey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 30.05.2015.
 */
public class EventOld extends FragmentActivity {
    private static final String URI = "uri";
    private static final String CHECKS = "checks";
    private static final String ADDR = "addr";
    private static final String EVENT_NAME = "eventName";
    private static final char PLUS = '+';
    private static final String DATE = "date";
    private static String URI_ADD = "/event";
    private final String LOGIN = "login";
    private RequestQueue queue;
    private TextView tvName;
    private String uri;
    private String LOG = "my con";
    private TextView tvOut;
    private TextView tvAddr;
    private TextView tvDate;
    private String stPath;
    private String login;
    private String eventName;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_checked_in);
        eventName = noPros(getIntent().getStringExtra(EVENT_NAME));
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }

        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
        uri = stPath + URI_ADD + login;
        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvDate.setText(params.get(DATE));
                tvName.setText(noPros(params.get(EVENT_NAME)));
                tvAddr.setText(noPros(params.get(ADDR)));

                double longitude = Double.parseDouble(params.get("myWidth"));
                double latitude = Double.parseDouble(params.get("myHeight"));
                makeMyPoint(latitude, longitude);

                Log.d(LOG, params.get(CHECKS) + "is param CHECKS");
                if (params.get(CHECKS) != null) {
                    Log.d(LOG, "coordrinates = " + params.get(CHECKS));
                    String[] coords = params.get(CHECKS).split(" ");
                    for (String coord : coords) {
                        String[] c = coord.split("-");
                        if (!c[0].equals("")) {
                            Log.d(LOG, "making blue marker");
                            longitude = Double.parseDouble(c[0]);
                            latitude = Double.parseDouble(c[1]);
                            makeOtherPoint(latitude, longitude);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText("Connecction problem, check your network");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(EVENT_NAME, eventName);
                params.put(LOGIN, login);

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

    public void onBackPressed() {
        Intent myIntent = new Intent(this, LeftPanel.class);
        myIntent.putExtra(LOGIN, login);
        myIntent.putExtra(URI, stPath);
        startActivity(myIntent);
    }

    public void makeOtherPoint(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public void makeMyPoint(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(4)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }

    public void onIncludeList(View v){
        Intent incList = new Intent(this, IncludeList.class);
        incList.putExtra(LOGIN, login);
        incList.putExtra(URI, stPath);
        incList.putExtra(EVENT_NAME, noPros(eventName));
        startActivity(incList);
    }

    public void onAlbum(View v){
        Intent eventInt = new Intent(getApplicationContext(), Album.class);
        eventInt.putExtra(LOGIN, login);
        eventInt.putExtra(URI, stPath);
        eventInt.putExtra(EVENT_NAME, noPros(eventName));
        startActivity(eventInt);
    }
}