package ru.startandroid.develop.p1391googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FutureMeetings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.future_meetings_activity);
	}
	
	public void onClickCheckIn(View view){
		Intent intent = new Intent(this, MapsActivity.class);
		startActivity(intent);
	}
}