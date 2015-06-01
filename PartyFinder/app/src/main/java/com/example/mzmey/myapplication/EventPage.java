package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventPage extends FragmentActivity implements View.OnClickListener {

    private static final String URI = "uri";
    private final String LOGIN = "login";
    private static final String MAKE_JOIN = "makeJoin";
    private static final String ADDR = "addr";
    private static final String EVENT_NAME = "eventName";
    private static final char PLUS = '+';
    private static final String DATE = "date";
    private static final String IN = "in";
    private static String URI_ADD = "/event";
    private RequestQueue queue;
    private TextView tvName;
    private Button checkOrJoin;
    private String uri;
    private String LOG = "my con";
    private TextView tvAddr;
    private TextView tvDate;
    private boolean invited = true;
    private String stPath;
    private String login;
    private String name;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        name = getIntent().getStringExtra(EVENT_NAME);
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);
        checkOrJoin = (Button) findViewById(R.id.checkOrJoin);
        checkOrJoin.setOnClickListener(this);
        tvDate = (TextView) findViewById(R.id.tvDate);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        uri = stPath + URI_ADD + login;
        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                if (params.get(IN).equals("1")) {
                    if (!params.get("myWidth").equals("0") && !params.get("myHeight").equals("0")) {
                        Intent checkInt = new Intent(getApplicationContext(), EventOld.class);
                        checkInt.putExtra(LOGIN, login);
                        checkInt.putExtra(URI, stPath);
                        checkInt.putExtra(EVENT_NAME, name);
                        startActivity(checkInt);
                        Log.d(LOG, "myWidth, myHeight = " + params.get("myWidth") + ", " + params.get("myHeight"));
                    }
                } else {
                    checkOrJoin.setText("Вступить");
                    invited = false;
                }
                String date = params.get(DATE);
                String eventName = params.get(EVENT_NAME);
                String addr = params.get(ADDR);
                if (date != null)
                    tvDate.setText(params.get(DATE));
                if (eventName != null)
                    tvName.setText(params.get(noPros(EVENT_NAME)));
                if (addr != null)
                    tvAddr.setText(params.get(ADDR));
                Log.d(LOG, "name, date, addr = " + eventName + ", " + date + ", " + addr);

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
                params.put(EVENT_NAME, name);
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

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, LeftPanel.class);
        myIntent.putExtra(LOGIN, login);
        myIntent.putExtra(URI, stPath);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG, "on click");
        if (invited == false) {

            Log.d(LOG, "вступление");

            StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        checkOrJoin.setText("Чек ин");
                        invited = true;
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
                    params.put(EVENT_NAME, name);
                    params.put(LOGIN, login);
                    params.put(MAKE_JOIN, "YES");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            queue.add(sr);
        } else {
            Log.d(LOG, "going on the other intent");
            Intent checkIntMake = new Intent(getApplicationContext(), MapsActivity.class);
            checkIntMake.putExtra(LOGIN, login);
            checkIntMake.putExtra(URI, stPath);
            checkIntMake.putExtra(EVENT_NAME, name);
            startActivity(checkIntMake);
        }
    }

    public void onAlbum(View v){
        Intent eventInt = new Intent(getApplicationContext(), EventPage.class);
        eventInt.putExtra(LOGIN, login);
        eventInt.putExtra(URI, stPath);
        eventInt.putExtra(EVENT_NAME, name);
        startActivity(eventInt);
    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }
}
