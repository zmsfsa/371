package com.example.mzmey.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 25.05.2015.
 */
public class SearchPage extends Fragment {
    private View search;
    private ImageButton btFndUsr;
    private ImageButton ibFndEvnt;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        search = inflater.inflate(R.layout.search_page, container, false);
        super.onCreate(savedInstanceState);
        btFndUsr = (ImageButton)search.findViewById(R.id.ibFndUs);
        btFndUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), FindFriend.class);
                myIntent.putExtra("login", getActivity().getIntent().getStringExtra("login"));
                myIntent.putExtra("uri", getActivity().getIntent().getStringExtra("uri"));
                getActivity().startActivity(myIntent);
            }
        });

        ibFndEvnt = (ImageButton)search.findViewById(R.id.ibFndEvnt);
        ibFndEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), FindParty.class);
                myIntent.putExtra("login", getActivity().getIntent().getStringExtra("login"));
                myIntent.putExtra("uri", getActivity().getIntent().getStringExtra("uri"));
                getActivity().startActivity(myIntent);
            }
        });

        return search;

    }
}
