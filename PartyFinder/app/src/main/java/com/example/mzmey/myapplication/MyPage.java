package com.example.mzmey.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyPage extends Fragment implements View.OnClickListener {
    public class PushParams{
        public PushParams(int p, byte[] b, String uri){
            this.port = p;
            this.img = b;
            this.path = uri;
        }

        public int port;
        public byte[] img;
        public String path;
    }

    private static final String PHOTO = "photo";
    private static final String EVENTS = "events";
    private static final String LOGIN = "login";
    private static final int BUFF_L = 1000;
    private static final int GALLERY_REQUEST = 1;
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String PHONE = "phone";
    private static final String ALBUM = "album";
    private static final String URI_ADD = "/myPage";
    private static final String ADD_PHOTO = "addPhoto";
    private static final String DEL = "/";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String URI = "uri";
    private String uri;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private View rootview;
    private String login;
    private TextView tvName;
    private ImageView ivFace;
    private TextView tvPhone;
    private boolean left = true;
    private Button btAdd;
    private String stPath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_page, container, false);
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        tvName = (TextView) rootview.findViewById(R.id.tvName);
        tvPhone = (TextView) rootview.findViewById(R.id.tvPhone);
        leftL = (LinearLayout) rootview.findViewById(R.id.leftL);
        rightL = (LinearLayout) rootview.findViewById(R.id.rightL);
        ivFace = (ImageView) rootview.findViewById(R.id.ivFace);
        btAdd = (Button) rootview.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        uri = intent.getStringExtra(URI) + URI_ADD + login;
        queue = MyQueue.getInstance(rootview.getContext().getApplicationContext()).getQueue();



        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvName.setText(params.get(LNAME) + " " + params.get(FNAME));
                tvPhone.setText(params.get(PHONE));
                String[] events = params.get(EVENTS).split(",");
                for (String event : events){
                    String[] pair = event.split("-");
                    cookView(pair[0], Integer.parseInt(pair[1]));
                    Log.d("my con", "name = " + pair[0] + ", int = " + Integer.parseInt(pair[1]));
                }
                if (!params.get(PHOTO).equals("0")) {
                    continueLoading(Integer.parseInt(params.get(PHOTO)), ivFace);
                    Log.d("my connection", "id of photo is " + params.get(PHOTO));
                } else
                    Log.d("my connection", "param is 0");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText("connection problem, check your network");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(ADD_PHOTO, "NO");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>();
            }
        };

        queue.add(sr);


        return rootview;
    }

    private void cookView(String AName, int id) {
        ImageView ivEvent = new ImageView(this.getActivity());
        TextView tvAName = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        LinearLayout.LayoutParams lParamsT = new LinearLayout.LayoutParams(param, 70);
        continueLoading(id, ivEvent);
        tvAName.setText(AName);
        if (left) {
            leftL.addView(tvAName, lParamsT);
            leftL.addView(ivEvent, lParamsI);
            left = false;
        } else {
            rightL.addView(tvAName, lParamsT);
            rightL.addView(ivEvent, lParamsI);
            left = true;
        }
    }

    public void onClick(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);/*
        final Bitmap myB = BitmapFactory.decodeResource(getResources(), R.drawable.abc_ic_search);
        continueSending(myB);*/

    }

    public byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public Bitmap getBitmapfromByteArray(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap galleryPic = null;

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("my connection", String.format("bitmap size = %sx%s, byteCount = %s",
                            galleryPic.getWidth(), galleryPic.getHeight(),
                            (int) (galleryPic.getByteCount() / 1024)));
                    int height = galleryPic.getHeight();
                    int width = galleryPic.getWidth();
                    int c = height / 150;
                    height /= c;
                    width /= c;
                    Bitmap sendB = Bitmap.createScaledBitmap(galleryPic, width,
                            height, false);
                    Log.d("my connection", String.format("bitmap size = %sx%s, byteCount = %s",
                            sendB.getWidth(), sendB.getHeight(),
                            (int) (sendB.getByteCount() / 1024)));
                    continueSending(sendB);
                }
        }
    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(getActivity().getIntent().getStringExtra(URI) + "/photo?" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        //tvName.setText("photo problem");
                    }
                });
        queue.add(request);

    }


    private void continueSending(final Bitmap myB) {

        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyPush myt = new MyPush();
                PushParams par = new PushParams(Integer.parseInt(response), getByteArrayfromBitmap(myB), "192.168.0.106");
                myt.execute(par);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText("connection problem, check your network");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(ADD_PHOTO, "YES");

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

    class MyPush extends AsyncTask<PushParams, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(PushParams... params) {
            Socket soc = null;
            PushParams par = params[0];
            byte[] img = par.img;
            int port = par.port;
            String path = par.path;
            try {
                soc = new Socket(path, port);

                OutputStream out = soc.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                dos.writeInt(img.length);
                dos.write(img, 0, img.length);

                dos.close();
                out.close();

                Log.d("my connection", "sent " + img.length + " bytes");
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


    }

}