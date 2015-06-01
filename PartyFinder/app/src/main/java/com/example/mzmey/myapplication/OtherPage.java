package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * Created by MZmey on 26.05.2015.
 */
public class OtherPage extends Activity {

    private static final String PHOTO = "photo";
    private static final String LOGIN = "login";
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String EVENTS = "events";
    private static final String URI = "uri";
    private static final String IS_FRIEND = "isFriend";
    private static final String URI_ADD = "/otherPage";
    private static final String EVENT_NAME = "eventName";
    private static final String ADD_FRIEND = "addFriend";
    private static final String FNAME = "fName";
    private static final String DELETE_FRIEND = "deleteFriend";
    private static final String LNAME = "lName";
    private static final String BIRTH = "birth";
    private static final String PHONE = "phone";

    private String login;
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private TextView tvBirth;
    private String LOG = "my con";
    private TextView tvName;
    private ImageView ivFace;
    private TextView tvPhone;
    private RequestQueue queue;
    private boolean left = true;
    private Button btAdd;
    private String otherLogin;
    private String stPath;
    private String uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_page);
        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        otherLogin = intent.getStringExtra(OTHER_LOGIN);
        stPath = intent.getStringExtra(URI);
        uri = stPath + URI_ADD + login;
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvBirth = (TextView) findViewById(R.id.tvBirth);
        leftL = (LinearLayout) findViewById(R.id.leftL);
        rightL = (LinearLayout) findViewById(R.id.rightL);
        ivFace = (ImageView) findViewById(R.id.ivFace);
        btAdd = (Button) findViewById(R.id.btAdd);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvName.setText(params.get(LNAME) + " " + params.get(FNAME));
                tvName.setTextSize(33);
                if (params.get(IS_FRIEND).equals("1")) {
                    btAdd.setText("Удалить из друзей");
                    btAdd.setOnClickListener(new DeleteClick());
                } else {
                    btAdd.setText("Добавить в друзья");
                    btAdd.setOnClickListener(new AddClick());
                }
                tvPhone.setText(params.get(PHONE));
                tvBirth.setText(params.get(BIRTH));

                Log.d(LOG, params.get(LNAME) + " " + params.get(FNAME) + " photo " + params.get(PHOTO) + " friend "  + params.get(IS_FRIEND)
                        + " phone " + params.get(PHONE) + " " + params.get(BIRTH));
                if (!params.get(PHOTO).equals("0")) {
                    continueLoading(Integer.parseInt(params.get(PHOTO)), ivFace);
                    Log.d("my connection", "id of photo is " + params.get(PHOTO));
                }

                Log.d("my con", "events = " + params.get(EVENTS));
                if (params.get(EVENTS) != null)
                    if (!params.get(EVENTS).equals("")) {
                        Log.d(LOG, "params(EVENTS) is '" + params.get(EVENTS) + "'");
                        String[] events = params.get(EVENTS).split(",");
                        for (String event : events) {
                            if (!event.equals("")) {
                                String[] pair = event.split("-");
                                cookView(pair[0], Integer.parseInt(pair[1]));
                                Log.d("my con", "name = " + pair[0] + ", int = "
                                        + Integer.parseInt(pair[1]));
                            }
                        }
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG, "error in start");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(OTHER_LOGIN, otherLogin);
                Log.d(LOG, "sent in start");

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

    private void cookView(final String aName, int id) {
        ImageView ivEvent = new ImageView(this);
        TextView tvAName = new TextView(this);
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        LinearLayout.LayoutParams lParamsT = new LinearLayout.LayoutParams(param, 70);
        if(id != 0)
            continueLoading(id, ivEvent);
        tvAName.setText(aName);
        ivEvent.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        ivEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumInt = new Intent(getApplicationContext(), Album.class);
                albumInt.putExtra(LOGIN, login);
                albumInt.putExtra(EVENT_NAME, aName);
                albumInt.putExtra(URI, stPath);
                startActivity(albumInt);
            }
        });

        tvName.setTextSize(18);
        ivEvent.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        if (left) {
            leftL.addView(tvAName, lParamsT);
            leftL.addView(ivEvent, lParamsI);
            left = false;
        } else {
            rightL.addView(tvAName, lParamsT);
            rightL.addView(ivEvent, lParamsI);
            left = true;
        }
    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (bitmap != null)
                            iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //tvName.setText("photo problem");
                    }
                });
        queue.add(request);
    }


    public class AddClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StringRequest addSr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(LOG, "in response add");
                    btAdd.setText("Удалить из друзей");
                    btAdd.setOnClickListener(new DeleteClick());
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
                    params.put(OTHER_LOGIN, otherLogin);
                    params.put(ADD_FRIEND, "YES");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            queue.add(addSr);
        }

    }

    public class DeleteClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            StringRequest delSr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    btAdd.setText("Добавить в друзья");
                    btAdd.setOnClickListener(new AddClick());
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
                    params.put(OTHER_LOGIN, otherLogin);
                    params.put(DELETE_FRIEND, "YES");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            queue.add(delSr);
        }
    }
}
