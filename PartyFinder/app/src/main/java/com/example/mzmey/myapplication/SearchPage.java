package com.example.mzmey.myapplication;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 25.05.2015.
 */
public class SearchPage extends Fragment implements View.OnClickListener {
    private View rootview;
    private static final String LOGIN = "login";
    private static final String AIM = "aim";
    private static final String USER = "user";
    private static final String EVENT = "event";
    private static String uri = "http://93.175.7.110:8080/friends";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String NAME = "Name";
    private Button btUser;
    private Button btEvent;
    private LinearLayout userL;
    private LinearLayout eventL;
    private String login;
    private RequestQueue queue;
    private StringRequest sr;
    private EditText edUser;
    private EditText edEvent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.search_page, container, false);

        queue = MyQueue.getInstance(this.getActivity().getApplicationContext()).getQueue();
        edUser = (EditText)rootview.findViewById(R.id.edUser);
        userL = (LinearLayout)rootview.findViewById(R.id.userL);
        eventL = (LinearLayout)rootview.findViewById(R.id.eventL);
        edEvent = (EditText)rootview.findViewById(R.id.edEvent);
        btUser = (Button) rootview.findViewById(R.id.btUser);
        btEvent = (Button) rootview.findViewById(R.id.btEvent);
        btUser.setOnClickListener(this);
        btEvent.setOnClickListener(this);
        login = getActivity().getIntent().getStringExtra(LOGIN);

        return rootview;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btUser:
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
                        params.put(AIM, USER);
                        params.put(NAME, edUser.getText().toString());

                        return params;
                    }
                };

                queue.add(sr);
                break;
            case R.id.btEvent:
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
                        params.put(AIM, EVENT);
                        params.put(NAME, edEvent.getText().toString());

                        return params;
                    }
                };

                queue.add(sr);
                break;
            default:
                break;
        }
    }

    private void cookView(String user, String event){
        userL.removeAllViews();
        eventL.removeAllViews();
        TextView tvUser = new TextView(this.getActivity());
        TextView tvEvent = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(param, 400);
        tvUser.setText(user);
        tvEvent.setText(event);
        userL.addView(tvUser, lParams);
        eventL.addView(tvEvent, lParams);
    }
}
