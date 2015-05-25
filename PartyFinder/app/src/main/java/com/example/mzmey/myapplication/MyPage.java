package com.example.mzmey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MyPage extends Fragment implements View.OnClickListener{
    private static final String LOGIN = "login";
    private View rootview;
    private String login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.my_page, container, false);

        super.onCreate(savedInstanceState);
        ImageButton btn = (ImageButton) rootview.findViewById(R.id.imageButton);
        btn.setOnClickListener(this);
        login = getActivity().getIntent().getStringExtra(LOGIN);

        return rootview;
    }

    public void onClick(View v){

    }
}