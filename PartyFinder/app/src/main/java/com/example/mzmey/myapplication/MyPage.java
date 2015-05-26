package com.example.mzmey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class MyPage extends Fragment implements View.OnClickListener{
    private static final String LOGIN = "login";
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String PHONE = "phone";
    private static final String ALBUM = "album";
    private static final String DEL = "/";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String URI = "uri";
    private String uri;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private StringRequest sr;
    private View rootview;
    private String login;
    private TextView tvName;
    private TextView tvPhone;
    private boolean left;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        tvName = (TextView)rootview.findViewById(R.id.tvName);
        tvPhone = (TextView)rootview.findViewById(R.id.tvPhone);
        leftL = (LinearLayout)rootview.findViewById(R.id.leftL);
        rightL = (LinearLayout)rootview.findViewById(R.id.rightL);
        login = intent.getStringExtra(LOGIN);
        uri = intent.getStringExtra(URI);

        queue = MyQueue.getInstance(rootview.getContext().getApplicationContext()).getQueue();
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvName.setText(params.get(LNAME) + " " + params.get(FNAME));
                tvPhone.setText(params.get(PHONE));
                String albums = params.get(ALBUM);
                String[] oneA = albums.split(DEL);
                for(String a : oneA){
                    Map<String, String> par = Mapper.queryToMap(a);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText("connection problem, check your network");
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

        return rootview;
    }

    private void cookView(String picture, String aName){
        ImageView ivFace = new ImageView(this.getActivity());
        TextView tvAName = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 400);
        tvAName.setText(aName);
        if(left) {
            leftL.addView(ivFace, lParams);
            leftL.addView(tvAName, lParams);
        }else{
            rightL.addView(ivFace, lParams);
            rightL.addView(tvAName, lParams);
        }
    }

    public void onClick(View v){

    }
}