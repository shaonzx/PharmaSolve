package com.mti.pharmasolve;




import com.mti.pharmasolve.session.SessionManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Dashboard extends Activity {
	
	
	
	Button btnOrder;
	Button btnQueueOrder;
	Button btnTakePicture;
	Button btnQueuedPicture;
	Button btnLogOut;
	Button btnReports;
	
	
	LocationManager aLocationManager;
	
	
	
	private void InitializeComponant()
	{		
		btnTakePicture = (Button) findViewById(R.id.dashboard_btnTakePicture);
		btnOrder = (Button) findViewById(R.id.dashboard_btnOrder);
		btnQueueOrder = (Button) findViewById(R.id.dashboard_btnQueuedOrder);
		btnQueuedPicture = (Button) findViewById(R.id.dashboard_btnQueuedPicture);
		btnLogOut = (Button) findViewById(R.id.dashboard_btnLogOut);
		btnReports = (Button) findViewById(R.id.dashboard_btnReports);
		
	}

	public void ShowSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Dashboard.this.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
	}
	
	public boolean CanGetLocation()
	{
		LocationManager aLocationManager = (LocationManager) Dashboard.this.getSystemService(LOCATION_SERVICE);
		
		boolean isGPSEnabled, isNetworkEnabled;
		
		// getting GPS status
        isGPSEnabled = aLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
                
        // getting network status
        isNetworkEnabled = aLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if (isGPSEnabled && isNetworkEnabled)
        {
        	return true;
        }else
        {
        	return false;
        }
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		InitializeComponant();

		
		SessionManager session = new SessionManager(getApplicationContext());
		
		if(!session.IsServiceStarted())
		{
			if(CanGetLocation())
			{
				SessionManager mySession = new SessionManager(getApplicationContext());
				mySession.FlagServiceStart();
				
				Intent aStartService = new Intent(getBaseContext(), LocationService.class);			
				startService(aStartService);
				
			}else{
				ShowSettingsAlert();
			}
		}
		
		
		btnTakePicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent startCam = new Intent(Dashboard.this, TakePicture.class);
				startActivity(startCam);				
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				Intent startProductList = new Intent(Dashboard.this, ProductList.class);
				
				startActivity(startProductList);
			}
		});
		
		btnQueueOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startQueuedList = new Intent(Dashboard.this, QueuedOrderList.class);
				startActivity(startQueuedList);
			}
		});
		
		btnQueuedPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startQueuedList = new Intent(Dashboard.this, QueuedImageList.class);
				startActivity(startQueuedList);
			}
		});
	
		btnLogOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(new Intent(getBaseContext(), LocationService.class));
				
				SessionManager session = new SessionManager(getApplicationContext());
				session.LogoutUser();
				finish();				
			}
		});
		
		btnReports.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent startReports = new Intent(Dashboard.this, Reports.class);
				startActivity(startReports);
			}
		});
	}	
	
	
	
	
}
