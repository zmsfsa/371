package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;


public class StartPage extends ActionBarActivity implements OnClickListener  {
    Button button_reg;
    Button button_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_page);
        button_reg = (Button) findViewById(R.id.button_reg);
        button_enter = (Button) findViewById(R.id.button_enter);
        button_reg.setOnClickListener(this);
        button_enter.setOnClickListener(this);
        button_enter.setPressed(true);

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_enter:
                Intent intent1 = new Intent(this, LogPage.class);
                startActivity(intent1);
                break;
            case R.id.button_reg:
                Intent intent2 = new Intent(this, RegPage.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
