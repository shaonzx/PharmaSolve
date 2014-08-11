
package com.mti.pharmasolve;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import com.mti.pharmasolve.session.SessionManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductList extends Activity {
	
	static int ORDER_ID;

	LinearLayout ll;
	LinearLayout[] llx;
	TextView[] tx;
	EditText[] ex;
	CheckBox[] cx;
	Button btnQueueOrder;
	Button btnPlaceOrder;
	Spinner spnrCustomerId;
	
	static String USER_ID;

	private double grandTotal;

	List<Products> productList;
	List<String> customerIdList;
	private ProgressDialog aProgressDialog;
	private AlertDialog dlgAlert;
	private static final String PRODUCTS_URL = "http://pharmasolve.apiary-mock.com/Products";

	DatabaseHelper db;

	private void InitializeComponant() {
		spnrCustomerId = (Spinner) findViewById(R.id.product_list_spnrCustomerId);
		llx = new LinearLayout[productList.size()];
		tx = new TextView[productList.size()];
		ex = new EditText[productList.size()];
		cx = new CheckBox[productList.size()];
		// txtGrandTotal.setText("Grand Total: BDT 0");
	}

	private void InitializeBefore() {
		
		SessionManager session = new SessionManager(getApplicationContext());
		USER_ID = session.GetUserIdFromSharedPreferences();		
		
		db = new DatabaseHelper(getApplicationContext());
		btnPlaceOrder = (Button) findViewById(R.id.product_list_btnPlaceOrder);
		btnQueueOrder = (Button) findViewById(R.id.product_list_btnQueue);

		customerIdList = new ArrayList<String>();
		productList = new ArrayList<Products>();
		ll = (LinearLayout) findViewById(R.id.mylinear);
	}

	private void ShowEmptyCustomerIdAlert() {
		android.app.AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
				ProductList.this);

		dlgAlert.setMessage("Please Select Customer Id!");
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
				
				if (spnrCustomerId.getSelectedItem().toString().equals("<Select>")) {
					ShowEmptyCustomerIdAlert();
				} else {
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				//Date d = new Date();
				//String strDate = formater.format(d);
				String dateTime = formatter.format(new Date());
				
				String orderNumber = GenerateOrderNumber();
				
				
				
				//System.out.println("Order Number: " + orderNumber);
				
				new OrderMaster().execute(dateTime, spnrCustomerId.getSelectedItem().toString(), orderNumber, USER_ID);
				// 
				//System.out.println("Order Number: " + orderNumber);
				
				}
				
			}

			private String GenerateOrderNumber() {
				// TODO Auto-generated method stub
				
				
				String orderNumber = null;
				
				try {
					orderNumber = new OrderNumber().execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				return orderNumber;
			}
		});

		btnQueueOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (spnrCustomerId.getSelectedItem().toString().equals("<Select>")) {
					ShowEmptyCustomerIdAlert();
				} else {
					Customers_DB aCustomers_DB = new Customers_DB(spnrCustomerId.getSelectedItem().toString());

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
								
								String customerId = spnrCustomerId.getSelectedItem().toString();
								String productName = tx[i].getText().toString();
								int quantity = Integer.parseInt(ex[i].getText().toString());
								Products myProduct = productList.get(i);
								double salesPrice = myProduct.GetSalesPrice();
								String productId = myProduct.GetProductCode();
								
								Products_DB aProducts_DB = new Products_DB(customerId, productName, quantity, salesPrice, productId);
								
								long someProducts = db.InsertProduct(aProducts_DB);
								//System.out.println("Some Products: " + someProducts);
							}
						}

					}
					
					Intent success = new Intent(ProductList.this, SuccessfulQueue.class);
					startActivity(success);
					finish();

				}
			}
		});

		//new PopulateProductList().execute();
		customerIdList.add("<Select>");
		new CustomerListPopulation().execute();
	}

	private void PopulateView() {
		
		ArrayAdapter<String> customerAdapter = new ArrayAdapter<String>(ProductList.this, android.R.layout.simple_list_item_1, customerIdList);
		customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spnrCustomerId.setAdapter(customerAdapter);
		
		
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

				@TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
	
	class CustomerListPopulation extends AsyncTask<Void, Void, Void>{
		
		JSONObject aJSONObject;
		JSONArray aJSONArray;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(ProductList.this);
			aProgressDialog.setMessage("Loading Customer List...");
			aProgressDialog.setIndeterminate(true);
			// aProgressDialog.setCancelable(false);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			RestAPI api = new RestAPI();
			
			try {
				aJSONObject = api.GetCustomerIdList();
				aJSONArray = aJSONObject.getJSONArray("Value");
				int len = aJSONArray.length();
								
				for(int I=0; I<len; I++)
				{
					JSONObject mJSONObject = null;
					mJSONObject = aJSONArray.getJSONObject(I);
					customerIdList.add(mJSONObject.getString("customerId"));					
				}
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
			
			new PopulateProductList().execute();
		}
		
		
		
	}
	
	class PopulateProductList extends AsyncTask<Void, Void, Void> {
		
		JSONObject productsObject;
		
		
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
			RestAPI api = new RestAPI();
			
			try {
				productsObject = api.GetProductDetails();
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
				JSONArray aJSONArray = productsObject.getJSONArray("Value");
				int numberOfProducts = aJSONArray.length();
				
				for(int I=0; I<numberOfProducts; I++)
				{
					Products aProducts = null;
					
					JSONObject mJSONObject = aJSONArray.getJSONObject(I);
					aProducts = new Products(mJSONObject.getString("productName"), mJSONObject.getString("productId"), Double.parseDouble(mJSONObject.getString("salesPrice")));
					productList.add(aProducts);
				}				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Toast.makeText(ProductList.this, "Products: " + productObject.toString(), Toast.LENGTH_LONG).show();
			System.out.println("Products: " + productsObject.toString());
			
			InitializeComponant();
			PopulateView();
		}		
	}
	
	class OrderMaster extends AsyncTask<String, Void, Void>{
		JSONObject aJSONObject;

				
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(ProductList.this);
			aProgressDialog.setMessage("Placing Order...");
			aProgressDialog.setIndeterminate(true);
			// aProgressDialog.setCancelable(false);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			RestAPI api = new RestAPI();
			try {
				aJSONObject = api.InsertOrderMaster(params[0], params[1], params[2], params[3]);
				ORDER_ID = aJSONObject.getInt("Value");
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
			//aProgressDialog.dismiss();
			System.out.println("Date: " + aJSONObject.toString());
			new OrderDetails().execute();
		}
		
		
	}
	
	class OrderDetails extends AsyncTask<Void, Void, Void> {
		
		JSONObject resultObject;
		String insertQuery;

		private String BuildQueryString()
		{
			String query = "INSERT INTO OrderDetails (ProdId, Qty, TotalSp, OrdId) VALUES ";
			boolean nothing = true;
			int size = productList.size();
			
			
			for (int i = 0; i < size; i++) {				
				
				if (cx[i].isChecked()) {
					if (!ex[i].getText().toString().equals("")) 
					{
						//Get Product Object
						Products aProducts = productList.get(i);
						
						//get ProductId
						String productId = aProducts.GetProductCode();
						
						//Get Quantity
						int quantity = Integer.parseInt(ex[i].getText().toString());
						
						//Calculate total sales price
						double totalSalesPrice = aProducts.GetSalesPrice() * quantity;
						
						if(nothing)
						{
							query = query + "('" + productId + "', '" + quantity + "', '"+ totalSalesPrice +"', '"+ ORDER_ID +"')";
							nothing = false;
						}
						else
						{
							query = query + ", ('" + productId + "', '" + quantity + "', '"+ totalSalesPrice +"', '"+ ORDER_ID +"')";
						}
						
					}
				}

			}
			query = query + ";";
			return query;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			insertQuery = BuildQueryString();			
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			RestAPI api = new RestAPI();
			try {
				resultObject = api.InsertOrderDetails(insertQuery);
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
			System.out.println("Result: " + resultObject.toString());
			
			
			int rows = 0;
			boolean success = false;
			
			try {
				rows = resultObject.getInt("Value");
				success = resultObject.getBoolean("Successful");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Query: " + insertQuery);
			
			if(success)
			{
				Toast.makeText(ProductList.this, rows + " product placed in order", Toast.LENGTH_LONG).show();
				
				Intent callDashboard = new Intent(ProductList.this, Dashboard.class);
				startActivity(callDashboard);
				finish();
			}
			else{
				Toast.makeText(ProductList.this, "Something went wrong! Please try again...", Toast.LENGTH_LONG).show();
			}
		}
		
		
	}
	
	class OrderNumber extends AsyncTask<Void, Void, String>{
		
		JSONObject aJSONObject;
		String orderNumber;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(ProductList.this);
			aProgressDialog.setMessage("Placing Order...");
			aProgressDialog.setIndeterminate(true);
			// aProgressDialog.setCancelable(false);
			aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			RestAPI api = new RestAPI();
			try {
				aJSONObject = api.GenerateOrderNo(spnrCustomerId.getSelectedItem().toString());
				orderNumber = aJSONObject.getString("Value");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return orderNumber;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//aProgressDialog.dismiss();		
			
		}		
		
	}
	
}
