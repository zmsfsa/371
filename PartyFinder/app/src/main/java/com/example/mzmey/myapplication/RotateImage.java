package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Федор on 31.05.2015.
 */
public class RotateImage extends FragmentActivity implements View.OnClickListener {

    Matrix matrix;
    ImageView imageView;
    Bitmap bitmap;
    Intent rotateIntent;
    Button btnClockwise;
    Button btnCounterClockwise;
    Button btnSaveChanges;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate_image);
        rotateIntent = getIntent();
        bitmap = (Bitmap) rotateIntent.getParcelableExtra("BitmapImage");
        imageView = (ImageView)findViewById(R.id.imageView4);
        imageView.setImageBitmap(bitmap);

        btnClockwise = (Button) findViewById(R.id.clockwise);
        btnCounterClockwise = (Button) findViewById(R.id.counterClockwise);
        btnSaveChanges = (Button) findViewById(R.id.saveChanges);

        btnClockwise.setOnClickListener(this);
        btnCounterClockwise.setOnClickListener(this);
        btnSaveChanges.setOnClickListener(this);
    }


    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.clockwise:
                matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                imageView.setImageBitmap(bitmap);
                intent.putExtra("Bitmap", bitmap);
                break;

            case R.id.counterClockwise:
                matrix = new Matrix();
                matrix.postRotate(270);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                imageView.setImageBitmap(bitmap);
                intent.putExtra("Bitmap", bitmap);
                break;

            case R.id.saveChanges:
                rotateIntent.putExtra("BitmapImage", bitmap);
                Log.d("my con", "" + bitmap.getByteCount());
                Toast.makeText(this, "Изменения сохранены!", Toast.LENGTH_SHORT);
                setResult(RESULT_OK, rotateIntent);
                finish();
                break;
        }
    }
}
