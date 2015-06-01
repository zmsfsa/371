package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

/**
 * Created by MZmey on 01.06.2015.
 */
public class IncludeList extends Activity {

    private static final String URI = "uri";
    private static final String URI_ADD = "/includeList";
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String EVENT_NAME = "eventName";
    private static final String PHOTO = "photo";
    private static final String LNAME = "lName";
    private static final String FNAME = "fName";
    private static final String LOGIN = "login";
    private static final int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String DEL = "/";

    private StringRequest sr;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private String login;
    private String uri;
    private String stPath;
    private String eventName;
    private String LOG = "my con";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_list);

        leftL = (LinearLayout) findViewById(R.id.leftL);
        rightL = (LinearLayout) findViewById(R.id.rightL);
        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        eventName = intent.getStringExtra(EVENT_NAME);
        queue = MyQueue.getInstance(getApplicationContext()).getQueue();

        uri = stPath + URI_ADD + login;
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                leftL.removeAllViews();
                rightL.removeAllViews();
                String[] users = response.split(DEL);

                for (String user : users) {

                    if (!user.equals("")) {
                        Map<String, String> params = Mapper.queryToMap(user);
                        if (params.get(OTHER_LOGIN).equals(login)) {

                            cookView(Integer.parseInt(params.get(PHOTO)), params.get(LNAME) + " " + params.get(FNAME), login);
                            Log.d(LOG, "added my photo " + params.get(OTHER_LOGIN));
                        }
                    }
                }

                for (String user : users) {
                    if (!user.equals("")) {
                        Map<String, String> params = Mapper.queryToMap(user);
                        if (!params.get(OTHER_LOGIN).equals(login)) {
                            Log.d(LOG, "added another photo " + params.get(OTHER_LOGIN));
                            if (params.get(PHOTO) != null)
                                cookView(Integer.parseInt(params.get(PHOTO)), params.get(LNAME) + " " + params.get(FNAME), params.get(OTHER_LOGIN));
                            else
                                cookView(0, params.get(LNAME) + " " + params.get(FNAME), params.get(OTHER_LOGIN));
                        }
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
                Log.d(LOG, "sent sr");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>();
            }
        };

        Log.d(LOG, "before request in create");
        queue.add(sr);
    }

    private void cookView(int id, String name, final String otherLogin) {
        ImageView ivPhoto = new ImageView(this);
        TextView tvName = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(param, 400);
        if (id != 0)
            continueLoading(id, ivPhoto);

        tvName.setGravity(Gravity.CENTER_VERTICAL);
        tvName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        tvName.setText(name);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login.equals(otherLogin)) {
                    Intent myIntent = new Intent(v.getContext(), LeftPanel.class);
                    myIntent.putExtra(LOGIN, login);
                    myIntent.putExtra(URI, stPath);
                    startActivity(myIntent);
                } else {
                    Intent otherIntent = new Intent(v.getContext(), OtherPage.class);
                    otherIntent.putExtra(LOGIN, login);
                    otherIntent.putExtra(OTHER_LOGIN, otherLogin);
                    otherIntent.putExtra(URI, stPath);
                    startActivity(otherIntent);
                }
            }
        });

        leftL.addView(ivPhoto, lParams);
        rightL.addView(tvName, lParams);


    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.d(LOG, "in image response");
                        iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(request);

    }

}
