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

import com.mti.pharmasolve.session.SessionManager;

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

	String userId;
	Button btnLogin;
	EditText edtUname, edtPassword;

	SessionManager session;

	private ProgressDialog aProgressDialog;

	private void InitializeComponant() {
		btnLogin = (Button) findViewById(R.id.login_btnLogin);
		edtUname = (EditText) findViewById(R.id.login_edtUserId);
		edtPassword = (EditText) findViewById(R.id.login_edtPassword);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = new SessionManager(getApplicationContext());

		if (session.IsLoggedIn()) {
			
			// User is logged in, redirect to Dashboard
			Intent i = new Intent(Login.this, Dashboard.class);
			
			// Close all the activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Start Dashboard
			startActivity(i);
			
			finish();

		} else {

			setContentView(R.layout.login);

			InitializeComponant();

			btnLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String username = edtUname.getText().toString();
					String password = edtPassword.getText().toString();

					if ((username.length() < 1) || (password.length() < 1)) {
						Toast.makeText(Login.this,
								"User Id/Password is not valid!",
								Toast.LENGTH_LONG).show();
					} else {
						new AttemptLogin().execute(username, password);
					}

				}
			});
		}
	}

	class AttemptLogin extends AsyncTask<String, Void, Void> {

		String auth;
		JSONObject aJSONObject;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			aProgressDialog = new ProgressDialog(Login.this);
			aProgressDialog.setMessage("Logging in...");
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			RestAPI api = new RestAPI();

			try {
				aJSONObject = api.AuthenticateUser(params[0], params[1]);
				auth = aJSONObject.getString("Value");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			userId = params[0];

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			aProgressDialog.dismiss();
			
			session = new SessionManager(getApplicationContext());

			if (auth.equals("Authenticated")) {
				
				session.CreateLoginSession(userId);
				
								
				// user is not logged in redirect him to Login Activity
				Intent i = new Intent(Login.this, Dashboard.class);
				
				// Closing all the Activities
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				// Add new Flag to start new Activity
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				startActivity(i);
				
				finish();
				
			} else {
				Toast.makeText(Login.this, "Verdict: " + auth,
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
