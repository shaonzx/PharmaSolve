package com.mti.pharmasolve;

import java.util.List;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Products_DB;

import android.app.Activity;
import android.content.Intent;
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
	TextView[] tx;
	EditText[] ex;
	CheckBox[] cx;

	private String customerId;
	DatabaseHelper db;
	List<Products_DB> productsList;

	Button btnPlaceOrder;
	Button btnDiscardorder;

	private void InitializeComponant() {
		customerId = getIntent().getExtras().getString("putId");
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
				Toast.makeText(QueuedOrder.this, "Order will be placed", Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		db.close();
		finish();
	}

}
