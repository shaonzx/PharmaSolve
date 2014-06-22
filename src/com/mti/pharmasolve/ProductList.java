package com.mti.pharmasolve;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Customers_DB;
import com.mti.pharmasolve.model.Products_DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductList extends Activity {

	LinearLayout ll;
	LinearLayout[] llx;
	TextView[] tx;
	EditText[] ex;
	CheckBox[] cx;
	Button btnQueueOrder;
	Button btnPlaceOrder;
	EditText edtCustomerId;

	private double grandTotal;

	List<Products> productList;
	private ProgressDialog aProgressDialog;
	private AlertDialog dlgAlert;
	private static final String PRODUCTS_URL = "http://pharmasolve.apiary-mock.com/Products";

	DatabaseHelper db;

	private void InitializeComponant() {
		edtCustomerId = (EditText) findViewById(R.id.product_list_edtCustomerId);
		llx = new LinearLayout[productList.size()];
		tx = new TextView[productList.size()];
		ex = new EditText[productList.size()];
		cx = new CheckBox[productList.size()];
		// txtGrandTotal.setText("Grand Total: BDT 0");
	}

	private void InitializeBefore() {
		db = new DatabaseHelper(getApplicationContext());
		btnPlaceOrder = (Button) findViewById(R.id.product_list_btnPlaceOrder);
		btnQueueOrder = (Button) findViewById(R.id.product_list_btnQueue);

		productList = new ArrayList<Products>();
		ll = (LinearLayout) findViewById(R.id.mylinear);
	}

	private void ShowEmptyCustomerIdAlert() {
		android.app.AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				ProductList.this);

		dlgAlert.setMessage("Please enter customer Id!");
		dlgAlert.setTitle("Pharma Solve");
		dlgAlert.setIcon(R.drawable.fail);
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);

		InitializeBefore();

		btnPlaceOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ProductList.this, "Will Place Order Immediatly",
						Toast.LENGTH_SHORT).show();
			}
		});

		btnQueueOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edtCustomerId.getText().toString().equals("")) {
					ShowEmptyCustomerIdAlert();
				} else {
					Customers_DB aCustomers_DB = new Customers_DB(edtCustomerId
							.getText().toString());

					// Insert Into database
					long something = db.InsertCustomer(aCustomers_DB);
					//System.out.println("Something: " + something);
					

					int size = productList.size();
					for (int i = 0; i < size; i++) {

						if (cx[i].isChecked()) {
							if (!ex[i].getText().toString().equals("")) 
							{
//								System.out.println("Pro: "+ tx[i].getText().toString()+ "Quantity: "+ Integer.parseInt(ex[i].getText()
//												.toString()));
								
								String customerId = edtCustomerId.getText().toString();
								String productName = tx[i].getText().toString();
								int quantity = Integer.parseInt(ex[i].getText().toString());
								
								Products_DB aProducts_DB = new Products_DB(customerId, productName, quantity);
								
								long someProducts = db.InsertProduct(aProducts_DB);
								//System.out.println("Some Products: " + someProducts);
							}
						}

					}
					
					Intent success = new Intent(ProductList.this, SuccessfulQueue.class);
					startActivity(success);

				}
			}
		});

		new PopulateProductList().execute();
	}

	private void PopulateView() {
		int size = productList.size();

		boolean setcolor = true;

		for (int i = 0; i < size; i++) {

			llx[i] = new LinearLayout(this);
			//System.out.println("Size: " + size);
			tx[i] = new TextView(this);
			ex[i] = new EditText(this);
			cx[i] = new CheckBox(this);

			cx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f));
			llx[i].addView(cx[i]);
			tx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.6f));
			tx[i].setTextSize(20);
			tx[i].setTextColor(0xff000000);
			ex[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f));
			ex[i].setEnabled(false);
			ex[i].setClickable(false);
			tx[i].setText(productList.get(i).GetProductName());
			ex[i].setInputType(InputType.TYPE_CLASS_NUMBER);
			llx[i].setId(i);
			llx[i].setClickable(true);

			if (setcolor) {
				View root = llx[i].getRootView();
				root.setBackgroundColor(getResources().getColor(
						android.R.color.darker_gray));
				setcolor = false;
			} else {
				View root = llx[i].getRootView();
				root.setBackgroundColor(getResources().getColor(
						android.R.color.background_light));
				setcolor = true;
			}

			final int K = i;

			cx[K].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (((CheckBox) v).isChecked()) {
						// Toast.makeText(ProductList.this, "This is Checked",
						// Toast.LENGTH_SHORT).show();

						ex[K].setEnabled(true);
						ex[K].setClickable(true);
						ex[K].requestFocus();

						String strQuantity = ex[K].getText().toString();
						double quantity;

						if (strQuantity == null || strQuantity.isEmpty()) {

							quantity = 0.0;

						} else {
							quantity = Double.parseDouble(strQuantity);

						}
						// grandTotal = grandTotal +
						// (productList.get(K).GetSalesPrice() * quantity);
						// txtGrandTotal.setText("Grand Total: " + grandTotal);
						//System.out.println("Grand Total" + grandTotal);
					} else {
						// Toast.makeText(ProductList.this,
						// "This is Not Checked", Toast.LENGTH_SHORT).show();

						ex[K].setEnabled(false);
						ex[K].setClickable(false);
						ex[K].setText("");

					}
				}
			});

			final int j = i;
			llx[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					msg(tx[j].getText().toString());
				}
			});

			llx[i].addView(tx[i]);
			llx[i].addView(ex[i]);

			ll.addView(llx[i]);
		}
	}

	private void msg(String x) {
		Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
	}

	public void cagir(View view) {
		switch (view.getId()) {
		case R.id.product_list_btnPlaceOrder:
			for (int i = 0; i < ex.length; i++) {
				if (!ex[i].getText().toString().equals("")) {
					Log.e(tx[i].getText().toString(), ex[i].getText()
							.toString());
				}
			}
			break;

		default:
			break;
		}
	}

	class PopulateProductList extends AsyncTask<Void, Void, Void> {

		JSONArray aJsonArray;
		int responselength;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(ProductList.this);
			aProgressDialog.setMessage("Fetching Products...");
			aProgressDialog.setIndeterminate(true);
			// aProgressDialog.setCancelable(false);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();

			/*************** Fetch Product Data *****************/

			try {
				HttpGet httpGet = new HttpGet(PRODUCTS_URL);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();

				if (httpEntity != null) {

					InputStream inputStream = httpEntity.getContent();

					// Lecture du retour au format JSON
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					StringBuilder stringBuilder = new StringBuilder();

					String ligneLue = bufferedReader.readLine();
					while (ligneLue != null) {
						stringBuilder.append(ligneLue + " \n");
						ligneLue = bufferedReader.readLine();
					}
					bufferedReader.close();
					// Log.i("JSON", stringBuilder.toString());
					aJsonArray = new JSONArray(stringBuilder.toString());

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			aProgressDialog.dismiss();

			aProgressDialog.dismiss();
			responselength = aJsonArray.length();
			for (int I = 0; I < responselength; I++) {
				Products aProducts = null;
				try {
					JSONObject mJSONObject = aJsonArray.getJSONObject(I);
					aProducts = new Products(mJSONObject.getString("name"),
							mJSONObject.getString("code"),
							Double.parseDouble(mJSONObject
									.getString("salesprice")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				productList.add(aProducts);
			}

			InitializeComponant();
			PopulateView();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		db.close();
		finish();
	}
	
	

}
