package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogPage extends ActionBarActivity {

    private static final String LOGIN = "login";
    private static final String CONTINUE = "continue";
    private static final String URI = "uri";
    private static final String PWD = "pwd";
    private static final String uri = "http://192.168.0.20:8080";
    private static final String URI_ADD = "/log";
    private EditText edPwd;
    private TextView tvOut;
    private EditText edLog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_page);
        tvOut = (TextView) findViewById(R.id.tvOut);
        edLog = (EditText) findViewById(R.id.edLog);
        edPwd = (EditText) findViewById(R.id.edPwd);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

    }

    public void onClick(View v) {

        if ((edLog.getText().toString().length() == 0) || (edPwd.getText().toString().length() == 0))
            Toast.makeText(this, getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
        else {
            if ((hasE(edLog.getText().toString())) || (hasE(edPwd.getText().toString()))) {
                Toast.makeText(this, getString(R.string.enter_no_rus), Toast.LENGTH_SHORT).show();
                Log.d("mycon", "hasE ooo to " + (edLog.getText().toString()) + " = " + hasE(edLog.getText().toString()));

            }
            else {
                Log.d("mycon", "hasE to " + (edLog.getText().toString()) + " = " + hasE(edLog.getText().toString()));
                StringRequest sr = new StringRequest(Request.Method.POST, uri + URI_ADD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals(CONTINUE)) {
                            goNext();
                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        params.put(LOGIN, edLog.getText().toString());
                        params.put(PWD, edPwd.getText().toString());

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

    public void goNext() {
        Intent intent = new Intent(this, LeftPanel.class);
        intent.putExtra(LOGIN, edLog.getText().toString());
        intent.putExtra(URI, uri);
        startActivity(intent);
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
