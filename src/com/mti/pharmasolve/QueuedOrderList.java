package com.mti.pharmasolve;

import java.util.ArrayList;
import java.util.List;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Customers_DB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class QueuedOrderList extends Activity{
	
	
	DatabaseHelper db;
	List<Customers_DB> customers_DBList;
	List<String> customerIdList;
	ListView aListView;
	
	private void InitializeComponant()
	{	
		aListView = (ListView) findViewById(R.id.queued_order_list_listQueuedOrder);
		db = new DatabaseHelper(getApplicationContext());
		customers_DBList = db.getAllCustomers();
		customerIdList = new ArrayList<String>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queued_order_list);
		InitializeComponant();
		
		for (Customers_DB aCustomers_DB : customers_DBList) {
			customerIdList.add(aCustomers_DB.GetCustomerId());
		}	
		
		List<String> customerIdWithHead = new ArrayList<String>();
		
		for (String string : customerIdList) {
			//int index = customerIdWithHead.indexOf(string);
			customerIdWithHead.add("Customer Id: " + string);
		}
		
		final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, customerIdWithHead);
		aListView.setAdapter(adapter);
		
		aListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String id = customerIdList.get(arg2);
				//Toast.makeText(QueuedOrderList.this, id, Toast.LENGTH_SHORT).show();
				Intent queuedOrder = new Intent(QueuedOrderList.this, QueuedOrder.class);
				queuedOrder.putExtra("putId", id);
				startActivity(queuedOrder);				
			}			
		});
		
	}	
}
