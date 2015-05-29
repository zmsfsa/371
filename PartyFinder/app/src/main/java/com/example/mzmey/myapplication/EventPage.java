package com.example.mzmey.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventPage extends Activity {

    private static final String URI = "uri";
    private final String LOGIN = "login";
    private static final String DEL = "/";
    private static final String PHOTO = "photo";
    private static final String ADDR = "addr";
    private static final String EVENT_NAME = "eventName";
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String DATE = "date";
    private static final String IN = "in";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String NAME = "name";
    private static String URI_ADD = "/event";
    private RequestQueue queue;
    private TextView tvName;
    private LinearLayout leftL;
    private String LOG = "my con";
    private LinearLayout rightL;
    private TextView tvOut;
    private TextView tvAddr;
    private TextView tvDate;
    private String stPath;
    private boolean left = true;
    private String login;
    private String name;
    private ScrollView scUsers;
    private ArrayList<String> userList = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        name = getIntent().getStringExtra(NAME);
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);
        leftL = (LinearLayout)findViewById(R.id.leftL);
        rightL = (LinearLayout)findViewById(R.id.rightL);
        tvDate = (TextView) findViewById(R.id.tvDate);
        scUsers = (ScrollView) findViewById(R.id.scUsers);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        String uri = stPath + URI_ADD + login;
        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] letters = response.split(DEL);
                for (String letter : letters) {
                    Map<String, String> params = Mapper.queryToMap(letter);
                    if (params.get(IN) != null)
                        if (params.get(IN).equals("1"))
                            cookView(params.get(LNAME) + " " + params.get(FNAME), Integer.parseInt(params.get(PHOTO)));
                        else
                            ;
                    tvDate.setText(params.get(DATE));
                    tvName.setText(params.get(EVENT_NAME));
                    tvAddr.setText(params.get(ADDR));
                }

                for (String letter : letters) {
                    Map<String, String> params = Mapper.queryToMap(letter);
                    if (params.get(IN) == null)
                        cookView(params.get(LNAME) + " " + params.get(FNAME), Integer.parseInt(params.get(PHOTO)));
                }
                Log.d(LOG, "ready with onResponse");

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
                params.put("eventName", name);
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

    private void cookView(String uName, int id) {
        ImageView ivEvent = new ImageView(this);
        TextView tvAName = new TextView(this);
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        LinearLayout.LayoutParams lParamsT = new LinearLayout.LayoutParams(param, 70);
        if(id != 0)
            continueLoading(id, ivEvent);
        tvAName.setText(uName);
        if (left) {
            leftL.addView(tvAName, lParamsT);
            leftL.addView(ivEvent, lParamsI);
            left = false;
        } else {
            rightL.addView(tvAName, lParamsT);
            rightL.addView(ivEvent, lParamsI);
            left = true;
        }
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
}
