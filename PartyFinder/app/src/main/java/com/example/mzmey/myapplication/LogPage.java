package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LogPage extends ActionBarActivity {

    private static final String LOGIN = "login";
    private static final String URI = "uri";
    private static final String uri = "http://192.168.0.105:8081";
    private static final String URI_ADD = "/log";
    private EditText edPwd;
    private TextView tvOut;
    private EditText edLog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_page);
        tvOut = (TextView)findViewById(R.id.tvOut);
        edLog = (EditText)findViewById(R.id.edLog);
        edPwd = (EditText)findViewById(R.id.edPwd);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

    }

    public void onClick(View v){

        if ((edLog.getText().toString().length() == 0) || (edPwd.getText().toString().length() == 0))
            tvOut.setText("Введите нормальные данные");
        else{


            StringRequest sr = new StringRequest(Request.Method.POST, uri + URI_ADD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("continue")){
                        goNext();
                    }
                    else{
                        tvOut.setText(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tvOut.setText("Connecction problem, check your network");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("login", edLog.getText().toString());
                    params.put("pwd", edPwd.getText().toString());

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

    public void goNext(){
        Intent intent = new Intent(this, LeftPanel.class);
        intent.putExtra(LOGIN, edLog.getText().toString());
        intent.putExtra(URI, uri);
        startActivity(intent);
    }

}
