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

    }


}
