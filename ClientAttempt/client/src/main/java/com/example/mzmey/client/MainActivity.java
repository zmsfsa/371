package com.example.mzmey.client;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.Integer.*;


public class MainActivity extends Activity{

    EditText ip;
    EditText port;
    TextView hello;
    TextView current;
    Button butCon;
    Socket client = null;
    OutputStream OutputStream;
    InputStream inputStream;
    byte[] buf = new byte[1024];
    String InputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        hello = (TextView)findViewById(R.id.hello);
        butCon = (Button)findViewById(R.id.butCon);
        current = (TextView)findViewById(R.id.current);

        butCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip.getText().toString().length() == 0 || port.getText().toString().length() == 0){
                    current.setText("enter ip and port");
                    return;
                }

                try{
                    client = new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));
                    OutputStream = client.getOutputStream();
                    buf = new byte[1024];
                    inputStream = client.getInputStream();

                    inputStream.read(buf);
                    InputText = new String(buf);

                    current.setText("hello message was " + InputText);
                    client.close();


                } catch (UnknownHostException e) {
                    current.setText("very bad address: " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    current.setText("bad address: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
