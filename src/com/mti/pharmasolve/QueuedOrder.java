package com.mti.pharmasolve;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mti.pharmasolve.ProductList.OrderDetails;
import com.mti.pharmasolve.ProductList.OrderMaster;
import com.mti.pharmasolve.ProductList.OrderNumber;
import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Products_DB;
import com.mti.pharmasolve.session.SessionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueuedOrder extends Activity {

	LinearLayout ll;
	LinearLayout[] llx;
	TextView txtCustomerId;
	TextView[] tx;
	EditText[] ex;
	CheckBox[] cx;
	
	static String USER_ID;
	static int ORDER_ID;

	private String customerId;
	DatabaseHelper db;
	List<Products_DB> productsList;
	
	ProgressDialog aProgressDialog;

	Button btnPlaceOrder;
	Button btnDiscardorder;
	
	
	//List<Products> productList;

	private void InitializeComponant() {
		
		SessionManager aSession = new SessionManager(getApplicationContext());
		USER_ID = aSession.GetUserIdFromSharedPreferences();
		
		customerId = getIntent().getExtras().getString("putId");
		txtCustomerId = (TextView) findViewById(R.id.queued_order_txtCustomerId);
		txtCustomerId.setText("Customer Id: " + customerId);
		
		db = new DatabaseHelper(getApplicationContext());
		productsList = db.GetProducts(customerId);
		
		btnDiscardorder = (Button) findViewById(R.id.queued_order_btnDiscardOrder);
		btnPlaceOrder = (Button) findViewById(R.id.queued_order_btnPlaceOrder);

		ll = (LinearLayout) findViewById(R.id.queued_order_mylinear);
		llx = new LinearLayout[productsList.size()];
		tx = new TextView[productsList.size()];
		ex = new EditText[productsList.size()];
		cx = new CheckBox[productsList.size()];
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queued_order);

		InitializeComponant();
		PopulateView();
		
		btnDiscardorder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(QueuedOrder.this, "Customer and order will be discarded", Toast.LENGTH_SHORT).show();
				
				DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				db.deleteCustomer(customerId);
				db.deleteProducts(customerId);
				db.close();
				Intent discarded = new Intent(QueuedOrder.this, SuccessfulDiscard.class);
				startActivity(discarded);
			}
		});
		
		btnPlaceOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				//Date d = new Date();
				//String strDate = formater.format(d);
				String dateTime = formatter.format(new Date());
				
				String orderNumber = GenerateOrderNumber();
				
				new OrderMaster().execute(dateTime, customerId, orderNumber, USER_ID);
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

	}

	private void PopulateView() {
		int size = productsList.size();

		boolean setcolor = true;

		for (int i = 0; i < size; i++) {

			llx[i] = new LinearLayout(this);
			tx[i] = new TextView(this);
			ex[i] = new EditText(this);
			cx[i] = new CheckBox(this);

			cx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f));
			cx[i].setChecked(true);
			llx[i].addView(cx[i]);

			tx[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.6f));
			tx[i].setTextSize(20);
			tx[i].setTextColor(0xff000000);
			tx[i].setText(productsList.get(i).GetProductName());
			llx[i].addView(tx[i]);

			ex[i].setLayoutParams(new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f));
			ex[i].setEnabled(true);
			ex[i].setClickable(true);
			ex[i].setInputType(InputType.TYPE_CLASS_NUMBER);
			ex[i].setText(String.valueOf(productsList.get(i)
					.GetProductQuantity()));
			ex[i].setSelectAllOnFocus(true);
			llx[i].addView(ex[i]);

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

			ll.addView(llx[i]);

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

						// grandTotal = grandTotal +
						// (productList.get(K).GetSalesPrice() * quantity);
						// txtGrandTotal.setText("Grand Total: " + grandTotal);
						// System.out.println("Grand Total" + grandTotal);
					} else {
						// Toast.makeText(ProductList.this,
						// "This is Not Checked", Toast.LENGTH_SHORT).show();

						ex[K].setEnabled(false);
						ex[K].setClickable(false);
						ex[K].setText("");

					}
				}
			});

		}
	}

class OrderNumber extends AsyncTask<Void, Void, String>{
		
		JSONObject aJSONObject;
		String orderNumber;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			aProgressDialog = new ProgressDialog(QueuedOrder.this);
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
				aJSONObject = api.GenerateOrderNo(customerId);
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
			
			aProgressDialog.dismiss();		
			
		}		
		
	}
	

class OrderMaster extends AsyncTask<String, Void, Void>{
	JSONObject aJSONObject;

			
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		aProgressDialog = new ProgressDialog(QueuedOrder.this);
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
		//System.out.println("Date: " + aJSONObject.toString());
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
		int size = productsList.size();
		
		
		for (int i = 0; i < size; i++) {				
			
			if (cx[i].isChecked()) {
				if (!ex[i].getText().toString().equals("")) 
				{
					//Get Product Object
					Products_DB aProducts = productsList.get(i);
					
					//get ProductId
					String productId = aProducts.GetProductId();
					
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
			Toast.makeText(QueuedOrder.this, rows + " product placed in order", Toast.LENGTH_LONG).show();
			
			Intent callDashboard = new Intent(QueuedOrder.this, Dashboard.class);			
			startActivity(callDashboard);
			finish();
		}
		else{
			Toast.makeText(QueuedOrder.this, "Something went wrong! Please try again...", Toast.LENGTH_LONG).show();
		}
	}
	
	
}


}
