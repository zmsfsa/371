package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.OutputStream;
import java.sql.Blob;


public class EnterEnter extends ActionBarActivity {
    private EditText edLog;
    private EditText edPwd;
    private TextView tvOut;
    private int id;
    private final String uri = "http://93.175.7.110:8080/log?";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_enter);
        tvOut = (TextView)findViewById(R.id.tvOut);
        edLog = (EditText)findViewById(R.id.edLog);
        edPwd = (EditText)findViewById(R.id.edPwd);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

    }

    public void onClick(View v){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri + edLog.getText().toString() + "=" + edPwd.getText().toString(),
                new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        String resp = (String)response;
                        String check[] = resp.split("=");
                        if(check[0].equals("continue")){
                            if(check.length == 2)
                                goNext(Integer.parseInt(check[1]));
                            else
                                tvOut.setText("not good response");
                        }
                        else{
                            tvOut.setText("mistake: " + resp);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvOut.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    public void goNext(int id){
           tvOut.setText("mooving next with id = " + id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
