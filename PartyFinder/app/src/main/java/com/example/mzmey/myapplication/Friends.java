package com.example.mzmey.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Friends extends Fragment implements View.OnClickListener{
    private View rootview;
    private static final String DEL = "/";
    private static final String LOGIN = "login";
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String LOG = "my logs";
    private static final String URI_ADD = "/friends";
    private static final String URI = "uri";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private StringRequest sr;
    private String login;
    private Button btUpd;
    private String stPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_friends,container,false);
        leftL = (LinearLayout)rootview.findViewById(R.id.leftL);
        rightL = (LinearLayout)rootview.findViewById(R.id.rightL);
        btUpd = (Button)rootview.findViewById(R.id.btUpd);
        btUpd.setOnClickListener(this);
        queue = MyQueue.getInstance(rootview.getContext()).getQueue();
        Intent intent = getActivity().getIntent();
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);

        String uri = stPath + URI_ADD + login;
        Log.d(LOG, "uri = " + uri);
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] three = response.split(DEL);
                for(String a : three) {
                    Map<String, String> params = Mapper.queryToMap(a);
                    cookView(params.get(FNAME), params.get(LNAME));
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

    private void cookView(String fName, String lName){
        ImageView ivFace = new ImageView(this.getActivity());
        TextView tvName = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 400);
        tvName.setText(lName + " " + fName);
        LinearLayout oneL;

        leftL.addView(ivFace, lParams);
        rightL.addView(tvName, lParams);
    }

    @Override
    public void onClick(View v) {
        leftL.removeAllViews();
        rightL.removeAllViews();
        queue.add(sr);
    }


}
