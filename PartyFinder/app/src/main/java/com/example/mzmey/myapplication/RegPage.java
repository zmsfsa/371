package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
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


public class RegPage extends ActionBarActivity {

    private static final String LOGIN = "login";
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String PHONE = "phone";
    private static final String PWD = "pwd";
    private EditText edLog;
    private TextView tvOut;
    private EditText edPwd;
    private EditText edFName;
    private EditText edLName;
    private EditText edPhone;
    private String uri = "http://93.175.7.110:8080/reg";
    private  RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_page);
        tvOut = (TextView)findViewById(R.id.tvOut);
        edLog = (EditText )findViewById(R.id.edLog);
        edPwd = (EditText)findViewById(R.id.edPwd);
        edFName = (EditText)findViewById(R.id.edFName);
        edLName = (EditText)findViewById(R.id.edLName);
        edPhone = (EditText)findViewById(R.id.edPhone);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
    }

    public void ClickReg(View v) {

        if ((edLog.getText().toString().length() == 0) || (edPwd.getText().toString().length() == 0) ||
                (edFName.getText().toString().length() == 0) || (edLName.getText().toString().length() == 0))
            tvOut.setText("wrong parametrs");
        else {
            StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        tvOut.setText("You are registrated, " + edLog.getText().toString());
                        goNext();
                    } else {
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
                    params.put(LOGIN, edLog.getText().toString());
                    params.put(PWD, edPwd.getText().toString());
                    params.put(FNAME, edFName.getText().toString());
                    params.put(LNAME, edLName.getText().toString());
                    params.put(PHONE, edPhone.getText().toString());

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
        Intent intent1 = new Intent(this, LogPage.class);
        intent1.putExtra("login", edLog.getText().toString());
        startActivity(intent1);
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
