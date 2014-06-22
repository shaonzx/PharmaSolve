package com.mti.pharmasolve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuccessfulQueue extends Activity {
	
	private Button btnDashboard;
	private TextView txtSuccess;
	
	private void InitializeComponant()
	{
		btnDashboard = (Button) findViewById(R.id.successful_queue_btnDashboard);
		txtSuccess = (TextView) findViewById(R.id.successful_queue_txtSuccess);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.successful_queue);
		
		InitializeComponant();
		
		txtSuccess.setTextColor(0xff00ff00);
		txtSuccess.setTypeface(null, Typeface.BOLD);
		
		btnDashboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dashboard = new Intent(SuccessfulQueue.this, Dashboard.class);
				startActivity(dashboard);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	
	
}
