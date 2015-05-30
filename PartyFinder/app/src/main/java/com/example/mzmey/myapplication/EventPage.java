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
public class EventPage extends FragmentActivity {

    private static final String URI = "uri";
    private final String LOGIN = "login";
    private static final String MAKE_JOIN = "makeJoin";
    private static final String DEL = "/";
    private static final String PHOTO = "photo";
    private static final String ADDR = "addr";
    private static final String EVENT_NAME = "eventName";
    private static final String FNAME = "fName";
    private static final char PLUS = '+';
    private static final String LNAME = "lName";
    private static final String DATE = "date";
    private static final String IN = "in";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static String URI_ADD = "/event";
    private RequestQueue queue;
    private TextView tvName;
    private Button checkOrJoin;
    private String uri;
    private String LOG = "my con";
    private TextView tvOut;
    private TextView tvAddr;
    private Marker marker;
    private TextView tvDate;
    private boolean invited = true;
    private String stPath;
    private String login;
    private String name;
    private boolean left = true;
    private String height = null;
    private String width = null;
    private LinearLayout leftL;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private LinearLayout rightL;

    private ScrollView scUsers;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        name = getIntent().getStringExtra(EVENT_NAME);
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);
        checkOrJoin = (Button) findViewById(R.id.checkOrJoin);
        tvDate = (TextView) findViewById(R.id.tvDate);
        scUsers = (ScrollView) findViewById(R.id.scUsers);
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
                    }
                } else {
                    checkOrJoin.setText("Вступить");
                    invited = false;
                }
                tvDate.setText(params.get(DATE));
                tvName.setText(params.get(noPros(EVENT_NAME)));
                tvAddr.setText(params.get(ADDR));

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

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //tvName.setText("photo problem");
                    }
                });
        queue.add(request);

    }

    public void onCheckOrJoin(View v) {
        if (checkOrJoin.getText().toString().equals("Вступить")) {

            StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK"))
                        checkOrJoin.setText("Чек ин");
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
            Intent checkIntMake = new Intent(getApplicationContext(), MapsActivity.class);
            checkIntMake.putExtra(LOGIN, login);
            checkIntMake.putExtra(URI, stPath);
            checkIntMake.putExtra(EVENT_NAME, name);
            startActivity(checkIntMake);
        }
    }


    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }
}
