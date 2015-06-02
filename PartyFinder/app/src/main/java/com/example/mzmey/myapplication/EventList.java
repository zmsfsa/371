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

public class EventList extends Fragment implements View.OnClickListener {

    private static final String LOGIN = "login";
    private static final String PHOTO = "photo";
    private static final String DEL = "/";
    private static final char PLUS = '+';
    private static final String EVENT_NAME = "eventName";
    private static final String DATE = "date";
    private static final String URI = "uri";
    private RequestQueue queue;
    private static final String URI_ADD = "/event_list";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private Button btAdd;
    private LinearLayout middleL;
    private StringRequest sr;
    private View rootview;
    private Button btUpd;
    private String stPath;
    private static final String LOG = "my logs";
    private String login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.event_list, container, false);
        leftL = (LinearLayout) rootview.findViewById(R.id.leftL);
        rightL = (LinearLayout) rootview.findViewById(R.id.rightL);
        queue = MyQueue.getInstance(rootview.getContext()).getQueue();
        btUpd = (Button) rootview.findViewById(R.id.btUpd);
        btUpd.setOnClickListener(this);
        btAdd = (Button) rootview.findViewById(R.id.btAdd);
        middleL = (LinearLayout) rootview.findViewById(R.id.middleL);
        btAdd.setOnClickListener(this);
        Intent intent = getActivity().getIntent();
        login = intent.getStringExtra(LOGIN);

        stPath = intent.getStringExtra(URI);
        String uri = stPath + URI_ADD + login;
        Log.d(LOG, "uri = " + uri);
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] pair = response.split(DEL);
                for (String a : pair) {
                    Map<String, String> params = Mapper.queryToMap(a);
                    if (params.get(EVENT_NAME) != null)
                        cookView(params.get(EVENT_NAME), params.get(DATE), Integer.parseInt(params.get(PHOTO)));;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG, "bad");
                cookView("Connection problem, check your network.", "", 0);
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

    private void cookView(final String name, String date, int id) {
        final ImageView ivEvent = new ImageView(this.getActivity());
        TextView tvName = new TextView(this.getActivity());
        TextView tvDate = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 250);
        if (id != 0) {
            ImageRequest request = new ImageRequest(getActivity().getIntent().getStringExtra(URI) + "/photo?" + id,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            ivEvent.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            //tvName.setText("photo problem");
                        }
                    });
            queue.add(request);
        }
        tvName.setText(noPros(name));
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEvent = new Intent(getActivity(), EventPage.class);
                intEvent.putExtra(LOGIN, login);
                intEvent.putExtra(EVENT_NAME, noPros(name));
                intEvent.putExtra(URI, stPath);
                getActivity().startActivity(intEvent);
            }
        });
        tvName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        tvDate.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        tvName.setGravity(Gravity.CENTER);
        tvDate.setGravity(Gravity.CENTER);
        tvName.setTextSize(20);
        tvDate.setText("Дата" + ": " + date);
        leftL.addView(ivEvent, lParams);
        middleL.addView(tvName, lParams);
        rightL.addView(tvDate, lParams);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btAdd:
                Intent intent1 = new Intent(getActivity(), EventCreate.class);
                intent1.putExtra(LOGIN, login);
                intent1.putExtra(URI, stPath);
                getActivity().startActivity(intent1);
                break;
            case R.id.btUpd:
                leftL.removeAllViews();
                rightL.removeAllViews();
                middleL.removeAllViews();
                queue.add(sr);
                break;
            default:
                break;

        }

    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }

}
