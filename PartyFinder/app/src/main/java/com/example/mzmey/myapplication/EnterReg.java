package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class EnterReg extends ActionBarActivity {
    EditText edLog;
    TextView tvOut;
    EditText edPwd;
    EditText edFName;
    EditText edLName;
    Button btReg;
    private String uri = "http://93.175.7.110:8080/reg/";
    RequestQueue queue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_reg);
        tvOut = (TextView)findViewById(R.id.tvOut);
        edLog = (EditText )findViewById(R.id.edLog);
        edPwd = (EditText)findViewById(R.id.edPwd);
        edFName = (EditText)findViewById(R.id.edFName);
        edLName = (EditText)findViewById(R.id.edLName);
        btReg = (Button)findViewById(R.id.btReg);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
    }

    public void ClickReg(View v){

        String req = uri + edLog.getText().toString() + "-" +edPwd.getText().toString() + "-" +
                edFName.getText().toString() + "-" + edLName.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, req,
                new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        String a = (String)response;
                        if(a.equals("OK")){
                            tvOut.setText("You are registrated, " + edLog.getText().toString());
                            goNext();
                        }
                        else{
                            tvOut.setText("mistake: " + a);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvOut.setText("Connecction problem, check your network");
            }
        });
        queue.add(stringRequest);
    }

    public void goNext(){
        Intent intent1 = new Intent(this, EnterEnter.class);
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
