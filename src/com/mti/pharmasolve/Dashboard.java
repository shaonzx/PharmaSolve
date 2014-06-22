package com.mti.pharmasolve;

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
	
	double latitude = 0.0, longitude = 0.0;
	

	LocationManager aLocationManager;
	MyLocationListener aMyLocationListener;
	
	
	private void InitializeComponant()
	{
		btnTakePicture = (Button) findViewById(R.id.dashboard_btnTakePicture);
		btnOrder = (Button) findViewById(R.id.dashboard_btnOrder);
		btnQueueOrder = (Button) findViewById(R.id.dashboard_btnQueuedOrder);
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
	
	public boolean StartService() {
		try {
			//GetCordinates aGetCordinates = new GetCordinates();
			LocationUpdates aLocationUpdates = new LocationUpdates();
			aLocationUpdates.execute();
			return true;
		} catch (Exception error) {
			return false;
		}
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		InitializeComponant();
		
/*		if(CanGetLocation())
		{
			if (!StartService()) {
				Toast.makeText(Dashboard.this, "Service can not be Started at this moment, check your Setting!",
						Toast.LENGTH_SHORT).show();
			}
		}else{
			ShowSettingsAlert();
		}
		*/
		
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
	}	
	
	class LocationUpdates extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			aMyLocationListener = new MyLocationListener();
            aLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            aLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, aMyLocationListener);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			while (latitude == 0.0) {
				//System.out.println("Still Getting location...");
            }

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			
			//Dashboard.this.getMyLocationAddress();
			
//			progressDialog.dismiss();
//            Toast.makeText(GeolocationUI.this, "LATITUDE :" + latitude + " LONGITUDE :" + longitude, 
//           		Toast.LENGTH_SHORT).show();
		}

	}
	
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {

			int lat = (int) location.getLatitude(); // * 1E6);
			int log = (int) location.getLongitude(); // * 1E6);
			int acc = (int) (location.getAccuracy());

			String info = location.getProvider();
			try {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				//System.out.println("Latitude: " + latitude);
				Toast.makeText(Dashboard.this, "Latitude: " + latitude + " and Longitude: " + longitude, Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				e.printStackTrace();				
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i("OnProviderDisabled", "OnProviderDisabled");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i("onProviderEnabled", "onProviderEnabled");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.i("onStatusChanged", "onStatusChanged");

		}

	}
}
