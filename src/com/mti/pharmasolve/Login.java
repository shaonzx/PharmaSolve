package com.mti.pharmasolve;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class Login extends Activity {

	Button btnLogin;
	EditText edtUname, edtPassword;
	
	// testing from a real server:
	private static final String LOGIN_URL = "http://pharmasolve.apiary-mock.com/user/login";
	
	private ProgressDialog aProgressDialog;
	
	private void InitializeComponant()
	{
		btnLogin = (Button) findViewById(R.id.login_btnLogin);
		edtUname = (EditText) findViewById(R.id.login_edtUserId);
		edtPassword = (EditText) findViewById(R.id.login_edtPassword);
	}
	
	public JSONObject BuildJson(String passworddString, String usernameString) {

		JSONObject loginJasonData = new JSONObject();
		try {
			loginJasonData.put("password", passworddString);
			loginJasonData.put("username", usernameString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loginJasonData;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		InitializeComponant();
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String username= edtUname.getText().toString();
                String password= edtPassword.getText().toString();
                
                JSONObject aJSONObject = BuildJson(password, username);

                // Execute the AsyncLogin class
                new AsyncLogin().execute(aJSONObject);
			}
		});	
	}
	
	
	
	class AsyncLogin extends AsyncTask<JSONObject, Void, Void> {
		
		
		String stringResponse;
		JSONObject jsonResponse;
		int responselength;
		
		String userName=null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			aProgressDialog = new ProgressDialog(Login.this);
			aProgressDialog.setMessage("Logging in...");
			aProgressDialog.setIndeterminate(true);
			//aProgressDialog.setCancelable(false);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(JSONObject... jsonObjects) {
			// TODO Auto-generated method stub
			
			JSONObject aJsonObject = jsonObjects[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 10000);

			String json = aJsonObject.toString();

			try {

				HttpPost httppost = new HttpPost(LOGIN_URL);
				StringEntity se = new StringEntity(aJsonObject.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(se);

				HttpResponse response = httpclient.execute(httppost);				
				stringResponse = EntityUtils.toString(response.getEntity());
//				System.out.println("Json Data From doIn func: "
//						+ aJsonObject.toString() + "\nResponse: " + stringResponse);

			} catch (ClientProtocolException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
			
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			aProgressDialog.dismiss();
			try {
				jsonResponse = new JSONObject(stringResponse);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			int len = jsonResponse.length();
			System.out.println("Json Data: " + jsonResponse.toString());
			
			if(len == 2)
			{
				Intent startDasboard = new Intent(Login.this, Dashboard.class);
				startActivity(startDasboard);
				
			}
			else{
				Toast.makeText(Login.this, "Login Error!", Toast.LENGTH_SHORT).show();
			}
		}
		
				
	
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
