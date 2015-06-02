package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MZmey on 30.05.2015.
 */
public class FindParty extends Activity {

    private static final String PHOTO = "photo";
    private static final int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String URI = "uri";
    private static final String EVENT_NAME = "eventName";
    private static final String LOGIN = "login";
    private static final char PLUS = '+';
    private static final String DEL = "/";
    private static final String URI_ADD = "/search";
    private RequestQueue queue;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private String stPath;
    private String uri;
    private String login;
    private EditText edEvent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_party);
        Intent intent = getIntent();
        stPath = intent.getStringExtra(URI);
        login = intent.getStringExtra(LOGIN);
        leftL = (LinearLayout) findViewById(R.id.leftL);
        rightL = (LinearLayout) findViewById(R.id.rightL);
        edEvent = (EditText) findViewById(R.id.edEvent);
        uri = stPath + URI_ADD + login;
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
    }

    public void onFnd(View v) {
        leftL.removeAllViews();
        rightL.removeAllViews();
        if (edEvent.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
        else {
            if (hasE(edEvent.getText().toString()))
                Toast.makeText(this, getString(R.string.enter_no_rus), Toast.LENGTH_SHORT).show();
            else {
                StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String events[] = response.split(DEL);
                        if (response.length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.nothing), Toast.LENGTH_SHORT).show();
                        else
                            for (String event : events) {
                                Map<String, String> params = Mapper.queryToMap(event);
                                cookView(params.get(EVENT_NAME), Integer.parseInt(params.get(PHOTO)));
                            }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put(EVENT_NAME, edEvent.getText().toString());

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
        }
    }

    private void cookView(final String eName, int id) {
        ImageView ivEvent = new ImageView(this);
        TextView tvEName = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(param, 400);
        lParams.gravity = Gravity.CENTER_VERTICAL;
        lParams.gravity = Gravity.CENTER_HORIZONTAL;
        if (id != 0)
            continueLoading(id, ivEvent);
        tvEName.setText(noPros(eName));
        tvEName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEvent = new Intent(getApplicationContext(), EventPage.class);
                intEvent.putExtra(LOGIN, login);
                intEvent.putExtra(EVENT_NAME, eName);
                intEvent.putExtra(URI, stPath);
                startActivity(intEvent);
            }
        });
        ivEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEvent = new Intent(getApplicationContext(), EventPage.class);
                intEvent.putExtra(LOGIN, login);
                intEvent.putExtra(EVENT_NAME, eName);
                intEvent.putExtra(URI, stPath);
                startActivity(intEvent);
            }
        });
        tvEName.setGravity(Gravity.CENTER_VERTICAL);
        tvEName.setTextSize(20);
        tvEName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);

        leftL.addView(ivEvent, lParams);
        rightL.addView(tvEName, lParams);


    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
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
        queue.add(request);

    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }

    private static Pattern pattern0_9__a_z__A_Z = Pattern.compile(
            "[\\w\\u005F\\u002E]+", Pattern.UNICODE_CASE);

    public static boolean hasE(String str) {
        String[] a = str.split(" ");
        boolean res = false;
        for (String b : a) {
            Matcher m = pattern0_9__a_z__A_Z.matcher(b);
            if (m.matches()) {
                ;
            } else {
                return true;
            }
        }
        return res;
    }
}
