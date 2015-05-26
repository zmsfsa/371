package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 26.05.2015.
 */
public class OtherPage extends Activity {
    private static final String LOGIN = "login";
    private static final String DEL = "/";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String URI = "uri";
    private String uri;
    private RequestQueue queue;
    private StringRequest sr;
    private String login;
    private String otherLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_page);
        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        otherLogin = intent.getStringExtra(OTHER_LOGIN);
        uri = intent.getStringExtra(URI);

        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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

    private void cookView(){

    }
}
