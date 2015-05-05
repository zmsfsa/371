package com.example.mzmey.myapplication;

/**
 * Created by MZmey on 05.05.2015.
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class EnterPage extends ActionBarActivity implements OnClickListener  {
    Button button_reg;
    Button button_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_page);
        button_reg = (Button) findViewById(R.id.button_reg);
        button_enter = (Button) findViewById(R.id.button_enter);
        button_reg.setOnClickListener(this);
        button_enter.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_enter:
               // Intent intent1 = new Intent(this, enter_enter.class);
                //startActivity(intent1);
                // TODO Call second activity
                break;
            case R.id.button_reg:
               // Intent intent2 = new Intent(this, enter_reg.class);
                //startActivity(intent2);
                break;
            default:
                break;
        }

        /*OnClickListener clickreg = new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(enter_page.this, enter_enter.class);
                startActivity(intent);
            }
        };*/

        //button_reg.setOnClickListener(clickreg);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_page, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
