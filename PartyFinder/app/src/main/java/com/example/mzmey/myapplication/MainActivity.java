package com.example.mzmey.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mzmey.volleyclient.R;


public class MainActivity extends Activity {

    private TextView tvOut;
    private Button btTry;
    private static final String uri = "http://192.168.0.105:8080/app/maxim-zmeev";

    RequestQueue queue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        tvOut = (TextView)findViewById(R.id.tvOut);
        btTry = (Button)findViewById(R.id.btTry);
        queue = Volley.newRequestQueue(this);
    }

    public void onTry(View v){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        //byte[] b = (byte[])response;
                        //String out = new String(response);
                        String a = (String)response;
                        tvOut.setText(a);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    tvOut.setText("That didn't work!");
                    }
         });
        queue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_volley, menu);
        return true;
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
