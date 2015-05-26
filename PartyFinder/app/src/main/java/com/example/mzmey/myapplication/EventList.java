package com.example.mzmey.myapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
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

import java.util.HashMap;
import java.util.Map;

public class EventList extends Fragment implements View.OnClickListener {

    private static final String DEL = "/";
    private static final String NAME = "name";
    private static final String DATE = "date";
    private RequestQueue queue;
    private static String uri = "http://93.175.7.110:8080/event_list";
    private String login = "mzmey37";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private StringRequest sr;
    private View rootview;
    private Button btUpd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.event_list,container,false);
        leftL = (LinearLayout)rootview.findViewById(R.id.leftL);
        rightL = (LinearLayout)rootview.findViewById(R.id.rightL);
        queue = MyQueue.getInstance(rootview.getContext()).getQueue();
        btUpd = (Button)rootview.findViewById(R.id.btUpd);
        btUpd.setOnClickListener(this);
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
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

        return rootview;
    }
    private void cookView(String name, String date){
        TextView tvName = new TextView(this.getActivity());
        TextView tvDate = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 250);
        tvName.setText(name);
        tvDate.setText(DATE + ": " + date);
        leftL.addView(tvName, lParams);
        rightL.addView(tvDate, lParams);
    }

    public void onClick(View v){
        leftL.removeAllViews();
        rightL.removeAllViews();
        queue.add(sr);
    }

}
