package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LayoutDirection;
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
public class FindFriend extends Activity implements View.OnClickListener {

    private static final String URI = "uri";
    private static final int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String LOGIN = "login";
    private static final String PHOTO = "photo";
    private static final String DEL = "/";
    private static final String OTHER_LOGIN = "otherLogin";
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String URI_ADD = "/search";
    private RequestQueue queue;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private String stPath;
    private Button btFnd;
    private String uri;
    private String login;
    private EditText edFName;
    private EditText edLName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friends);

        Intent intent = getIntent();
        stPath = intent.getStringExtra(URI);
        login = intent.getStringExtra(LOGIN);
        leftL = (LinearLayout) findViewById(R.id.leftL);
        rightL = (LinearLayout) findViewById(R.id.rightL);
        btFnd = (Button) findViewById(R.id.btFnd);
        edFName = (EditText) findViewById(R.id.edFName);
        edLName = (EditText) findViewById(R.id.edLName);
        uri = stPath + URI_ADD + login;
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
        btFnd.setOnClickListener(this);
    }

    private void cookView(final String uName, int id, final String uLogin) {
        ImageView ivFace = new ImageView(this);
        TextView tvUName = new TextView(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(param, 300);
        if (id != 0)
            continueLoading(id, ivFace);
        tvUName.setText(uName);

        if (!uLogin.equals(""))
            ivFace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (login.equals(uLogin)) {
                        Intent myIntent = new Intent(v.getContext(), LeftPanel.class);
                        myIntent.putExtra(LOGIN, login);
                        myIntent.putExtra(URI, stPath);
                        startActivity(myIntent);
                    } else {
                        Intent otherIntent = new Intent(v.getContext(), OtherPage.class);
                        otherIntent.putExtra(LOGIN, login);
                        otherIntent.putExtra(OTHER_LOGIN, uLogin);
                        otherIntent.putExtra(URI, stPath);
                        startActivity(otherIntent);
                    }
                }
            });

        tvUName.setGravity(Gravity.CENTER_VERTICAL);
        tvUName.setTextSize(20);
        tvUName.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);

        leftL.addView(ivFace, lParams);
        rightL.addView(tvUName, lParams);
    }

    @Override
    public void onClick(View v) {
        leftL.removeAllViews();
        rightL.removeAllViews();
        if (edFName.getText().toString().equals("") && edLName.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
        else {
            if ((hasE(edFName.getText().toString())) || (hasE(edLName.getText().toString())))
                Toast.makeText(this, getString(R.string.enter_no_rus), Toast.LENGTH_SHORT).show();
            else {
                Log.d("mylog", " fname " + edFName.getText().toString() + ", lname " + edLName.getText().toString());
                StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String users[] = response.split(DEL);
                        if (response.length() == 0)
                            Toast.makeText(getApplicationContext(), getString(R.string.nothing), Toast.LENGTH_SHORT).show();
                        else
                            for (String user : users) {
                                Map<String, String> params = Mapper.queryToMap(user);
                                cookView(params.get(LNAME) + " " + params.get(FNAME), Integer.parseInt(params.get(PHOTO)), params.get(LOGIN));
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
                        params.put(FNAME, edFName.getText().toString());
                        params.put(LNAME, edLName.getText().toString());

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
                        Toast.makeText(getApplicationContext(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);

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
