package com.mti.pharmasolve;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mti.pharmasolve.session.SessionManager;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TargetProducts extends Activity {

	ProgressDialog aProgressDialog;
	TableLayout myTableLayout;
	String dateFrom, dateTo;

	boolean isReportOfCurrentMonth = false;

	void InitializeEverything() {
		myTableLayout = (TableLayout) findViewById(R.id.tblMain);

		try {
			dateFrom = getIntent().getExtras().getString("putFrom");
			dateTo = getIntent().getExtras().getString("putTo");
		} catch (Exception e) {
			isReportOfCurrentMonth = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_products);

		InitializeEverything();

		if (isReportOfCurrentMonth) {
			new ReportOfTheMonth().execute();
		} else {
			new ReportinDateRange().execute();
		}
	}

	class ReportOfTheMonth extends AsyncTask<Void, Void, Void> {
		JSONObject aJSONObject;
		JSONArray aJSONArray;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(TargetProducts.this);
			aProgressDialog.setMessage("Loading Report...");
			aProgressDialog.setIndeterminate(true);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			SessionManager session = new SessionManager(getBaseContext());
			String user = session.GetUserIdFromSharedPreferences();

			aJSONObject = new JSONObject();

			RestAPI api = new RestAPI();

			try {
				aJSONObject = api.ReportOfTheMonth(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
				aJSONArray = aJSONObject.getJSONArray("Value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int size = aJSONArray.length();

			for (int I = 0; I < size; I++) {

				String productName = null, soldQuantity = null, soldAmount = null;
				JSONObject mJSONObject;
				try {
					mJSONObject = aJSONArray.getJSONObject(I);
					productName = mJSONObject.getString("productName");
					soldQuantity = mJSONObject.getString("sellQuantity");
					soldAmount = mJSONObject.getString("totalPrice");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TableRow aRow = new TableRow(this);
				TableRow aRow = new TableRow(TargetProducts.this);

				aRow.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				TextView column1 = new TextView(TargetProducts.this);
				TextView column2 = new TextView(TargetProducts.this);
				TextView column3 = new TextView(TargetProducts.this);

				column1.setText(productName);
				column1.setGravity(Gravity.CENTER);
				column1.setPadding(2, 0, 2, 0);
				column1.setTextSize(15);
				column1.setBackgroundColor(Color.parseColor("#dcdcdc"));
				column1.setTextColor(Color.parseColor("#000000"));

				column2.setText(soldQuantity);
				column2.setGravity(Gravity.CENTER);
				column2.setPadding(2, 0, 2, 0);
				column2.setTextSize(15);
				column2.setBackgroundColor(Color.parseColor("#d3d3d3"));
				column2.setTextColor(Color.parseColor("#000000"));

				column3.setText(soldAmount);
				column3.setGravity(Gravity.CENTER);
				column3.setPadding(2, 0, 2, 0);
				column3.setTextSize(15);
				column3.setBackgroundColor(Color.parseColor("#cac9c9"));
				column3.setTextColor(Color.parseColor("#000000"));

				aRow.addView(column1);
				aRow.addView(column2);
				aRow.addView(column3);

				myTableLayout.addView(aRow);
			}

		}

	}

	class ReportinDateRange extends AsyncTask<Void, Void, Void> {

		JSONObject aJSONObject;
		JSONArray aJSONArray;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(TargetProducts.this);
			aProgressDialog.setMessage("Loading Report...");
			aProgressDialog.setIndeterminate(true);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SessionManager session = new SessionManager(getBaseContext());
			String user = session.GetUserIdFromSharedPreferences();

			aJSONObject = new JSONObject();

			RestAPI api = new RestAPI();

			try {
				aJSONObject = api.ReportinDateRange(user, dateFrom, dateTo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			aProgressDialog.dismiss();

			// Toast.makeText(TargetProducts.this, aJSONObject.toString(),
			// Toast.LENGTH_LONG).show();

			try {
				aJSONArray = aJSONObject.getJSONArray("Value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int size = aJSONArray.length();

			for (int I = 0; I < size; I++) {

				String productName = null, soldQuantity = null, soldAmount = null;
				JSONObject mJSONObject;
				try {
					mJSONObject = aJSONArray.getJSONObject(I);
					productName = mJSONObject.getString("productName");
					soldQuantity = mJSONObject.getString("sellQuantity");
					soldAmount = mJSONObject.getString("totalPrice");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TableRow aRow = new TableRow(this);
				TableRow aRow = new TableRow(TargetProducts.this);

				aRow.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				TextView column1 = new TextView(TargetProducts.this);
				TextView column2 = new TextView(TargetProducts.this);
				TextView column3 = new TextView(TargetProducts.this);

				column1.setText(productName);
				column1.setGravity(Gravity.CENTER);
				column1.setPadding(2, 0, 2, 0);
				column1.setTextSize(15);
				column1.setBackgroundColor(Color.parseColor("#dcdcdc"));
				column1.setTextColor(Color.parseColor("#000000"));

				column2.setText(soldQuantity);
				column2.setGravity(Gravity.CENTER);
				column2.setPadding(2, 0, 2, 0);
				column2.setTextSize(15);
				column2.setBackgroundColor(Color.parseColor("#d3d3d3"));
				column2.setTextColor(Color.parseColor("#000000"));

				column3.setText(soldAmount);
				column3.setGravity(Gravity.CENTER);
				column3.setPadding(2, 0, 2, 0);
				column3.setTextSize(15);
				column3.setBackgroundColor(Color.parseColor("#cac9c9"));
				column3.setTextColor(Color.parseColor("#000000"));

				aRow.addView(column1);
				aRow.addView(column2);
				aRow.addView(column3);

				myTableLayout.addView(aRow);
			}

		}

	}

}
