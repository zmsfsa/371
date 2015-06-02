package com.example.mzmey.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Friends extends Fragment implements View.OnClickListener{
    private View rootview;
    private static final String DEL = "/";
    private static final String LOGIN = "login";
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String FNAME = "fName";
    private static final String PHOTO = "photo";
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
                    if(params.get(FNAME) != null)
                        cookView(params.get(FNAME) + " " + params.get(LNAME), Integer.parseInt(params.get(PHOTO)), params.get(LOGIN));
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

    private void cookView(String uName, int id, final String log){
        ImageView ivFace = new ImageView(this.getActivity());
        TextView tvuName = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(param, 200);
        tvuName.setText(uName);
        if (id != 0) {
           continueLoading(id, ivFace);
        }

        tvuName.setGravity(Gravity.CENTER);
        tvuName.setTextSize(20);
        tvuName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);

        tvuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEvent = new Intent(getActivity(), OtherPage.class);
                intEvent.putExtra(LOGIN, login);
                intEvent.putExtra(OTHER_LOGIN, log);
                intEvent.putExtra(URI, stPath);
                getActivity().startActivity(intEvent);
            }
        });

        leftL.addView(ivFace, lParams);
        rightL.addView(tvuName, lParams);
    }

    @Override
    public void onClick(View v) {
        leftL.removeAllViews();
        rightL.removeAllViews();
        queue.add(sr);
    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == '+')
                c[i] = ' ';
        return new String(c);
    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(getActivity().getIntent().getStringExtra(URI) + "/photo?" + id,
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
        Log.d(LOG, "MyPage continueLoading");
        queue.add(request);

    }


}
