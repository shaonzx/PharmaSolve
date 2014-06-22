package com.mti.pharmasolve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuccessfulDiscard extends Activity {
	
	private Button btnDashboard;
	private TextView txtSuccess;
	
	private void InitializeComponant()
	{
		btnDashboard = (Button) findViewById(R.id.successful_discard_btnDashboard);
		txtSuccess = (TextView) findViewById(R.id.successful_discard_txtSuccess);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.successful_discard);
		
		InitializeComponant();
		
		txtSuccess.setTextColor(0xffff0000);
		txtSuccess.setTypeface(null, Typeface.BOLD);
		
		btnDashboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dashboard = new Intent(SuccessfulDiscard.this, Dashboard.class);
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
