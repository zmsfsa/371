package com.example.mzmey.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by MZmey on 21.05.2015.
 */
public class MyQueue{
    private static MyQueue mInstance;
    private Context mCont;
    private RequestQueue mQueue;

    private MyQueue (Context context){
        mCont = context;
        mQueue = getQueue();
    }

    public static MyQueue getInstance(Context context)
    {
        if (mInstance == null){
            mInstance = new MyQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getQueue(){
        if(mQueue == null)
            mQueue = Volley.newRequestQueue(mCont.getApplicationContext());
        return mQueue;
    }
}