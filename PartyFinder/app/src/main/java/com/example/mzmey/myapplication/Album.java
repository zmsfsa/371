package com.example.mzmey.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Album extends FragmentActivity{

    public class PushParams {
        public PushParams(int p, byte[] b, String uri) {
            this.port = p;
            this.img = b;
            this.path = uri;
        }

        public int port;
        public byte[] img;
        public String path;
    }

    private static final String PHOTO = "photo";
    private static final int PHOTO_LENGTH = 250;
    private static final String LOGIN = "login";
    private static final int ROTATE = 2;
    private static final int GALLERY_REQUEST = 1;
    private static final String IN = "in";
    private static final String URI = "uri";
    private static final String URI_ADD = "/album";
    private static final String DELIMETR = "=";
    private static final String ADD_PHOTO = "addPhoto";
    private static final String DEL = "/";
    private static final String DELETE_PHOTO = "deletePhoto";
    private static final String EVENT_NAME = "eventName";
    private static final int param = LinearLayout.LayoutParams.MATCH_PARENT;

    private StringRequest sr;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private RequestQueue queue;
    private String login;
    private String uri;
    private String stPath;
    private String eventName;
    private String LOG = "my con";
    private Button btAdd;
    private boolean left = true;
    private ArrayList<CheckBox> toDelete = new ArrayList<CheckBox>();
    private Button btDel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        Intent intent = getIntent();
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        eventName = intent.getStringExtra(EVENT_NAME);
        uri = stPath + URI_ADD + login;
        leftL = (LinearLayout)findViewById(R.id.leftL);
        rightL = (LinearLayout)findViewById(R.id.rightL);
        queue = MyQueue.getInstance(getApplicationContext()).getQueue();
        btDel = (Button)findViewById(R.id.btDel);
        btAdd = (Button)findViewById(R.id.btAdd);
        btDel.setOnClickListener(new FirstDel());


        sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "in responnse of sr");
                Map<String, String> forIn = Mapper.queryToMap(response);
                if(forIn.get(IN).equals("0")){
                    btDel.setVisibility(View.INVISIBLE);
                    btAdd.setVisibility(View.INVISIBLE);
                }

                String[] photos = response.split(DEL);
                for (String photo : photos) {
                    Map<String, String> params = Mapper.queryToMap(photo);
                    if (params.get(PHOTO) != null) {
                        cookView(Integer.parseInt(params.get(PHOTO)));
                        Log.d(LOG, "id of photo " + Integer.parseInt(params.get(PHOTO)));
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(EVENT_NAME, eventName);
                Log.d(LOG, "sent sr");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new HashMap<String, String>();
            }
        };

        queue.add(sr);

    }

    private void continueLoading(int id, final ImageView iv) {
        ImageRequest request = new ImageRequest(stPath + "/photo?" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.d(LOG, "in image response");
                        iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(request);

    }

    private void cookView(int id) {
        ImageView ivPhoto = new ImageView(this);
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        continueLoading(id, ivPhoto);

        if (left) {
            leftL.addView(ivPhoto, lParamsI);
            left = false;
        } else {
            rightL.addView(ivPhoto, lParamsI);
            left = true;
        }
    }

    private void cookCheckBox(int id){

        CheckBox delCheck = new CheckBox(this);
        delCheck.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        toDelete.add(delCheck);
        delCheck.setText("");
        delCheck.setId(id);
        if (left) {
            rightL.addView(delCheck);
        } else {
            leftL.addView(delCheck);
        }
    }


    public void onAdd(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void continueSending(final Bitmap myB) {

        StringRequest srSend = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyPush myt = new MyPush();

                String numerals = stPath;
                String numerals1 = numerals.substring("http://".length());
                String[] ur = numerals1.split(":");
                Log.d(LOG, "ip is " + ur[0]);

                PushParams par = new PushParams(Integer.parseInt(response), getByteArrayfromBitmap(myB), ur[0]);
                myt.execute(par);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG, "error in continue sending");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(LOGIN, login);
                params.put(ADD_PHOTO, "YES");
                params.put(EVENT_NAME, eventName);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };


        queue.add(srSend);
    }

    public byte[] getByteArrayfromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
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

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap galleryPic = null;

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
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
                    int c = height / PHOTO_LENGTH;
                    height /= c;
                    width /= c;
                    Bitmap sendB = Bitmap.createScaledBitmap(galleryPic, width,
                            height, false);
                    Log.d("my connection", String.format("bitmap size = %sx%s, byteCount = %s",
                            sendB.getWidth(), sendB.getHeight(),
                            (int) (sendB.getByteCount() / 1024)));
                    Intent rotateIntent = new Intent(this, RotateImage.class);
                    rotateIntent.putExtra("BitmapImage", sendB);
                    startActivityForResult(rotateIntent, ROTATE);
                }
                break;
            case ROTATE:
                if (resultCode == RESULT_OK) {
                    Intent rotateIntent = imageReturnedIntent;
                    Bitmap sendB = (Bitmap) rotateIntent.getParcelableExtra("BitmapImage");
                    Log.d(LOG, "sendB is " + sendB);
                    continueSending(sendB);
                }
                break;

        }
    }

    public void onEvent(View v){
        Intent checkInt = new Intent(getApplicationContext(), EventPage.class);
        checkInt.putExtra(LOGIN, login);
        checkInt.putExtra(URI, stPath);
        checkInt.putExtra(EVENT_NAME, eventName);
        startActivity(checkInt);
    }

    public void onUpdate(View v){
        left = true;
        leftL.removeAllViews();
        rightL.removeAllViews();
        queue.add(sr);

    }

    public class SecondDel implements View.OnClickListener {

        //deleting of photos
        @Override
        public void onClick(View v) {

            ArrayList<Integer> delList = new ArrayList<Integer>();
            for (CheckBox ch : toDelete) {
                if (ch.isChecked()) {
                    delList.add(ch.getId());
                }
            }
            final ArrayList<Integer> sendList = delList;

            StringRequest delR = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(LOG, "second response");
                    btDel.setText("Удалить фото");
                    left = true;
                    leftL.removeAllViews();
                    rightL.removeAllViews();
                    btDel.setOnClickListener(new FirstDel());
                    btDel.setText("Удалить фото");
                    queue.add(sr);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG, "error in second");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(LOGIN, login);
                    params.put(EVENT_NAME, eventName);
                    params.put(DELETE_PHOTO, "YES");
                    StringBuilder sendBuilder = new StringBuilder("");
                    for (Integer i : sendList)
                        sendBuilder.append(i.toString() + "-");
                    String send = new String(sendBuilder);
                    params.put(PHOTO, send);
                    Log.d(LOG, send + " was sent");
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new HashMap<String, String>();
                }
            };
            queue.add(delR);
        }
    }

    public class FirstDel implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            left = true;
            leftL.removeAllViews();
            rightL.removeAllViews();
            StringRequest preDelSr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(LOG, "first response");
                    String[] photos = response.split(DEL);
                    for (String photo : photos) {
                        Map<String, String> params = Mapper.queryToMap(photo);
                        if (params.get(PHOTO) != null) {
                            cookView(Integer.parseInt(params.get(PHOTO)));
                            cookCheckBox(Integer.parseInt(params.get(PHOTO)));
                            Log.d(LOG, "id of photo " + Integer.parseInt(params.get(PHOTO)));
                        }
                    }
                    btDel.setText("Удалить выбранное");
                    btDel.setOnClickListener(new SecondDel());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG, "error in first");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put(LOGIN, login);
                    params.put(EVENT_NAME, eventName);
                    Log.d(LOG, "delFirst was sent");
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return new HashMap<String, String>();
                }
            };
            queue.add(preDelSr);
        }
    }
}