package com.example.mzmey.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventPage extends Activity {

    private final String QUESTION_MARK = "?";
    private static String uri = "http://93.175.7.110:8080/event";
    private RequestQueue queue;
    private TextView tvName;
    private TextView tvOut;
    private TextView tvDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_event);
        tvOut = (TextView)findViewById(R.id.tvOut);
        tvName = (TextView)findViewById(R.id.tvName);
        String name = getIntent().getStringExtra("name");
        tvName.setText(name);
        tvDate = (TextView)findViewById(R.id.tvDate);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        String req = uri + QUESTION_MARK + name;
        StringRequest strReq = new StringRequest(Request.Method.GET, req,
                new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        String a = (String)response;
                            tvDate.setText(a);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvOut.setText("Connecction problem, check your network");
            }
        });

        queue.add(strReq);

    }
}
