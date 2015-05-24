package com.example.mzmey.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MyPage extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu2_layout, container, false);

        super.onCreate(savedInstanceState);
        //присвоили кнопку к кнопке на леяуте
        ImageButton btn = (ImageButton) rootview.findViewById(R.id.imageButton);
        //повесили на него листенера
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //переходим с первой на вторую активность
                Intent intent = new Intent(getActivity(), MyPage3.class);
                startActivity(intent);
            }
        });
        return rootview;
    }
}