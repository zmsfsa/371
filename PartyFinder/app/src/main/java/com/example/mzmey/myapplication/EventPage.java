package com.example.mzmey.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private final String DELIMETR = "=";
    private static final String DATE = "date";
    private static final String IN = "in";
    private static final String OUT = "out";
    private final String QUESTION_MARK = "?";
    private static String URI_ADD = "/event";
    private RequestQueue queue;
    private TextView tvName;
    private TextView tvOut;
    private TextView tvDate;
    private String login;
    private String name;
    private ScrollView scUsers;
    private ArrayList<String> userList = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_event);
        tvOut = (TextView)findViewById(R.id.tvOut);
        tvName = (TextView)findViewById(R.id.tvName);
        name = getIntent().getStringExtra("name");
        login = getIntent().getStringExtra("login");
        tvName.setText(name);
        tvDate = (TextView)findViewById(R.id.tvDate);
        scUsers = (ScrollView)findViewById(R.id.scUsers);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        String uri = getIntent().getStringExtra(URI) + getIntent().getStringExtra(URI_ADD) + login;
        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] words = response.split(DELIMETR);
                if(words[0].equals(DATE)) {
                    tvDate.setText(words[2]);
                }  else{
                    tvOut.setText(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvOut.setText("Connecction problem, check your network");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("eventName", name);
                params.put("login", login);

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
}
