package com.example.mzmey.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Album extends FragmentActivity {

    private static final String PHOTO = "photo";
    private static final String LOGIN = "login";
    private static final String ALBUM = "album";
    private static final String URI = "uri";
    private static final String URI_ADD = "/album";
    private static final String DEL = "/";
    private static final String EVENT_NAME = "eventName";
    private static final int param = LinearLayout.LayoutParams.MATCH_PARENT;


    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private String login;
    private String uri;
    private String stPath;
    private String eventName;
    private String LOG = "my con";
    private boolean left = true;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        eventName = intent.getStringExtra(EVENT_NAME);
        uri = stPath + URI_ADD + login;
        leftL = (LinearLayout)findViewById(R.id.leftL);
        rightL = (LinearLayout)findViewById(R.id.rightL);
        queue = MyQueue.getInstance(getApplicationContext()).getQueue();

        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] photos = response.split(DEL);
                for (String photo : photos) {
                    Map<String, String> params = Mapper.queryToMap(photo);
                    if (params.get(PHOTO) != null) {
                        cookView(Integer.parseInt(params.get(PHOTO)));
                        Log.d(LOG, "id of photo " + Integer.parseInt(params.get(PHOTO)));
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(EVENT_NAME, eventName);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>();
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
                    }
                });
        queue.add(request);

    }

    private void cookView(int id) {
        ImageView ivPhoto = new ImageView(this);
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        continueLoading(id, ivPhoto);
        if (left) {
            leftL.addView(ivPhoto, lParamsI);
            left = false;
        } else {
            rightL.addView(ivPhoto, lParamsI);
            left = true;
        }
    }
}