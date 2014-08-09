package com.mti.pharmasolve;

import com.mti.pharmasolve.session.SessionManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
	
	WakeLock wakeLock;
	LocationManager locationManager;
	
	static String USER_ID;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		SessionManager session = new SessionManager(getApplicationContext());
		
		USER_ID = session.GetUserIdFromSharedPreferences();
		
		PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");
		
		Log.e("Google", "Service Created");
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		
		
		Toast.makeText(this, "Hello " + USER_ID + "! The Service has been Started!", Toast.LENGTH_SHORT).show();
		
	    locationManager = (LocationManager) getApplicationContext()
	            .getSystemService(Context.LOCATION_SERVICE);

	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, listener);
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Toast.makeText(this, "Service Stopped!", Toast.LENGTH_SHORT).show();
		locationManager.removeUpdates(listener);
		locationManager = null;
	}
	
	private void showCoordinates(double latitude, double longitude) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Location: " + latitude + " " + longitude, Toast.LENGTH_SHORT).show();
	}
	
	
	private LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			Log.e("Google", "Location Changed");

	        if (location == null)
	            return;
	        
	        if (isConnectingToInternet(getApplicationContext())) {
	            
	            try {
	            	
	            	double latitude = location.getLatitude();
	            	double longitude = location.getLongitude();
	            	
	            	//System.out.println("Location: " + latitude + " " + longitude);
	            	
	            	new SendLocation().execute(latitude, longitude);
	            	
	            	showCoordinates(latitude, longitude);
	            	
	                //new LocationWebService().execute(latitude, longitude);
	                
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
			
		}



		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public static boolean isConnectingToInternet(Context _context) {
	    ConnectivityManager connectivity = (ConnectivityManager) _context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null) {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null)
	            for (int i = 0; i < info.length; i++)
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                    return true;
	                }

	    }
	    return false;
	}
	
	
	private class SendLocation extends AsyncTask<Double, Void, Void> {

		@Override
		protected Void doInBackground(Double... params) {
			// TODO Auto-generated method stub
			
			RestAPI api = new RestAPI();
			
			try {
				api.SetUserLocation(USER_ID, params[0], params[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}

}
