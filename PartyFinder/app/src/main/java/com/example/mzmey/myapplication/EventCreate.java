package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventCreate extends Activity{

    private final String QUESTION_MARK = "?";
    private final String DELIMETR = "=";
    private static String uri = "http://93.175.7.110:8080/event/create";
    private RequestQueue queue;
    private TextView tvOut;
    private EditText edEvent;
    private String login;
    private EditText edDate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        tvOut = (TextView)findViewById(R.id.tvOut);
        edEvent = (EditText)findViewById(R.id.edEvent);
        edDate = (EditText)findViewById(R.id.edDate);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();

    }

    public void onClick(View v){

        String req = uri + QUESTION_MARK + "mzmey37" + DELIMETR + edEvent.getText().toString() + DELIMETR + edDate.getText().toString();
        StringRequest strReq = new StringRequest(Request.Method.GET, req,
                new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        String a = (String)response;
                        if(a.equals("OK")){
                            tvOut.setText("created");
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
        queue.add(strReq);
    }

    public void onGoOn(View v){
        Intent intent = new Intent(this, EventPage.class);
        intent.putExtra("login", login);
        intent.putExtra("name", edEvent.getText().toString());
        startActivity(intent);
    }
}
