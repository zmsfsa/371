package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private static final String DEL = "/";
    private static final String NAME = "name";
    private static final String DATE = "date";
    RequestQueue queue;
    private static String uri = "http://93.175.7.110:8080/event_list";
    private String login = "mzmey37";
    int param = LinearLayout.LayoutParams.MATCH_PARENT;
    LinearLayout leftL;
    LinearLayout rightL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        leftL = (LinearLayout)findViewById(R.id.leftL);
        rightL = (LinearLayout)findViewById(R.id.rightL);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] pair = response.split(DEL);
                for(String a : pair) {
                    Map<String, String> params = Mapper.queryToMap(a);
                    cookView(params.get(NAME), params.get(DATE));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cookView("Connection problem, check your network.", "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
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
    private void cookView(String name, String date){
        TextView tvName = new TextView(this);
        TextView tvDate = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 250);
        tvName.setText(name);
        tvDate.setText(DATE + ": " + date);
        leftL.addView(tvName, lParams);
        rightL.addView(tvDate, lParams);
    }
}
