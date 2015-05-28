package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    static final int GALLERY_REQUEST = 1;
    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    private static final String DEL = "/";
    private static final String NAME = "name";
    private static final String DATE = "date";
    RequestQueue queue;
    private static String uri = "http://192.168.0.105:8082/my_page";
    private String login = "mzmey37";
    int param = LinearLayout.LayoutParams.MATCH_PARENT;
    LinearLayout leftL;
    StringBuilder outStr = new StringBuilder();
    TextView tvOut;
    LinearLayout rightL;
    public MyTask myT;
    ImageView ivP;
    Button btRd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        leftL = (LinearLayout)findViewById(R.id.leftL);
        tvOut = (TextView)findViewById(R.id.tvOut);
        ivP = (ImageView) findViewById(R.id.tvP);
        btRd = (Button) findViewById(R.id.btRd);

        MyPull mp = new MyPull();
        mp.execute();

    }

    public byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public void onRead(View v) {
        // TODO Auto-generated method stub
/*
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);*/

        Log.d("my connection","hello");

        myT = new MyTask();

        myT.execute(getByteArrayfromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_search)));
    }

    class MyPull extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(Void... params) {
            byte[] b = new byte[1];

            try{

                Socket socket = new Socket("192.168.0.105", 4444);
                Log.d("my connection", "created socket");

                InputStream inputStream = socket.getInputStream();
                DataInputStream dis = new DataInputStream(inputStream);
                Log.d("my connection", "opened stream");
                int len = dis.readInt();
                Log.d("my connection", "read int = " + len);

                b = new byte[len];
                dis.read(b);
                Log.d("my connection", "read from dis b = " + new String(b));

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                Log.d("my connection", "made streams");

                dos.writeInt(1);
                dos.close();
                os.close();
                dis.close();
                inputStream.close();

                Log.d("my connection", "closed streams");

            }catch(Exception e){
                Log.d("my connection", "exception exception");
            }
            return b;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            Log.d("my connection", "I am setting an image " + new String(result));
        }
    }

    class MyTask extends AsyncTask<byte[], String, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvOut.setText("nanaan1");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            outStr.append(values[0]);
            tvOut.setText(outStr);
        }

        @Override
        protected Void doInBackground(byte[]... params) {
            Socket soc = null;
            byte[] img = params[0];
            try {
                publishProgress("in try");
                soc = new Socket("192.168.0.105", 8081);
                publishProgress("connected");
                OutputStream out = soc.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                publishProgress("have length = " + img.length);
                dos.writeInt(img.length);
                dos.write(img, 0, img.length);

                dos.close();
                out.close();

            } catch (Exception e) {
                try {
                    soc.close();
                } catch (Exception a) {
                    a.printStackTrace();
                }
            } finally {
                try {
                    soc.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ivP.setImageResource(R.drawable.abc_ic_cab_done_holo_dark);
        }
    }


}
