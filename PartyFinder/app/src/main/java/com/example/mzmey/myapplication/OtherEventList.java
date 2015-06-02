package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by MZmey on 02.06.2015.
 */
public class OtherEventList extends Activity implements View.OnClickListener{

    private static final String LOGIN = "login";
    private static final String PHOTO = "photo";
    private static final String DEL = "/";
    private static final char PLUS = '+';
    private static final String EVENT_NAME = "eventName";
    private static final String DATE = "date";
    private static final String URI = "uri";
    private RequestQueue queue;
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String URI_ADD = "/event_list";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private Button btAdd;
    private LinearLayout middleL;
    private StringRequest sr;
    private String otherLogin;
    private Button btUpd;
    private String stPath;
    private static final String LOG = "my logs";
    private String login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_event_list);
        leftL = (LinearLayout) findViewById(R.id.leftL);
        rightL = (LinearLayout) findViewById(R.id.rightL);
        queue = MyQueue.getInstance(getApplicationContext()).getQueue();
        btUpd = (Button) findViewById(R.id.btUpd);
        btAdd = (Button) findViewById(R.id.btAdd);
        middleL = (LinearLayout) findViewById(R.id.middleL);
        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        btUpd.setOnClickListener((this));
        otherLogin = intent.getStringExtra(OTHER_LOGIN);
        String uri = stPath + URI_ADD + login;
        Log.d(LOG, "uri = " + uri);
        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] pair = response.split(DEL);
                for (String a : pair) {
                    Map<String, String> params = Mapper.queryToMap(a);
                    if (params.get(EVENT_NAME) != null)
                        cookView(params.get(EVENT_NAME), params.get(DATE), Integer.parseInt(params.get(PHOTO)));
                    ;
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
                params.put("login", otherLogin);

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

    private void cookView(final String name, String date, int id) {
        final ImageView ivEvent = new ImageView(this);
        TextView tvName = new TextView(this);
        TextView tvDate = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                param, 250);
        if (id != 0) {
            ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
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
                Intent intEvent = new Intent(getApplicationContext(), EventPage.class);
                intEvent.putExtra(LOGIN, login);
                intEvent.putExtra(EVENT_NAME, noPros(name));
                intEvent.putExtra(URI, stPath);
                startActivity(intEvent);
            }
        });
        tvName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        tvDate.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        tvName.setGravity(Gravity.CENTER);
        tvDate.setGravity(Gravity.CENTER);
        tvName.setTextSize(20);
        tvDate.setText(getString(R.string.date)+ date);
        leftL.addView(ivEvent, lParams);
        middleL.addView(tvName, lParams);
        rightL.addView(tvDate, lParams);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
