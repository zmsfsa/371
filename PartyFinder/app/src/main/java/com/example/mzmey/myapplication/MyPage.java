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
    private static final String EVENTS = "events";
    private static final int ROTATE = 2;
    private static final String LOGIN = "login";
    private static final char PLUS = '+';
    private static final int GALLERY_REQUEST = 1;
    private static final String FNAME = "fName";
    private static final String LNAME = "lName";
    private static final String BIRTH = "birth";
    private static final String PHONE = "phone";
    private static final String URI_ADD = "/myPage";
    private static final String EVENT_NAME = "eventName";
    private static final String ADD_PHOTO = "addPhoto";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String URI = "uri";
    private String uri;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private TextView tvBirth;
    private RequestQueue queue;
    StringRequest myPageSr;
    private View rootview;
    private String login;
    private String LOG = "my con";
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
        tvBirth = (TextView) rootview.findViewById(R.id.tvBirth);
        login = intent.getStringExtra(LOGIN);
        stPath = intent.getStringExtra(URI);
        uri = stPath + URI_ADD + login;
        queue = MyQueue.getInstance(rootview.getContext().getApplicationContext()).getQueue();


        myPageSr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvName.setText(params.get(LNAME) + " " + params.get(FNAME));
                tvName.setTextSize(33);
                tvPhone.setText(params.get(PHONE));
                tvBirth.setText(params.get(BIRTH));
                Log.d("my con", "events = " + params.get(EVENTS));
                if (params.get(EVENTS) != null)
                    if (!params.get(EVENTS).equals("")) {
                        Log.d(LOG, "params(EVENTS) is '" + params.get(EVENTS) + "'");
                        String[] events = params.get(EVENTS).split(",");
                        for (String event : events) {
                            if (!event.equals("")) {
                                String[] pair = event.split("-");
                                cookView(noPros(pair[0]), Integer.parseInt(pair[1]));
                                Log.d("my con", "name = " + pair[0] + ", int = "
                                        + Integer.parseInt(pair[1]));
                            }
                        }
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

        queue.add(myPageSr);


        return rootview;
    }

    private void cookView(final String aName, int id) {
        ImageView ivEvent = new ImageView(this.getActivity());
        TextView tvAName = new TextView(this.getActivity());
        LinearLayout.LayoutParams lParamsI = new LinearLayout.LayoutParams(param, 400);
        LinearLayout.LayoutParams lParamsT = new LinearLayout.LayoutParams(param, 70);
        if(id != 0)
            continueLoading(id, ivEvent);
        tvAName.setText(aName);
        ivEvent.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
        ivEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumInt = new Intent(getActivity(), Album.class);
                albumInt.putExtra(LOGIN, login);
                albumInt.putExtra(EVENT_NAME, aName);
                albumInt.putExtra(URI, stPath);
                startActivity(albumInt);
            }
        });

        tvName.setTextSize(18);
        ivEvent.setBackgroundResource(R.drawable.abc_cab_background_top_holo_light);
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
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

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
                    Intent rotateIntent = new Intent(getActivity(), RotateImage.class);
                    rotateIntent.putExtra("BitmapImage", sendB);
                    startActivityForResult(rotateIntent, ROTATE);
                }
                break;
            case ROTATE:
                if (resultCode == getActivity().RESULT_OK) {
                    Intent rotateIntent = imageReturnedIntent;
                    Bitmap sendB = (Bitmap) rotateIntent.getParcelableExtra("BitmapImage");
                    Log.d(LOG, "sendB is " + sendB);
                    continueSending(sendB);
                }
                break;
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

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

        }

    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }


}