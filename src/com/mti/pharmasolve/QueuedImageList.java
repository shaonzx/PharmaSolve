package com.mti.pharmasolve;

import java.util.ArrayList;
import java.util.List;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Images_DB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class QueuedImageList extends Activity {
	
	private DatabaseHelper db;
	private ListView aListView;
	private List<Images_DB> imagesList;
	private List<String> displayContent;
	
	
	private void InitializeComponant()
	{
		db = new DatabaseHelper(getApplicationContext());
		aListView = (ListView) findViewById(R.id.queued_image_list_listQueuedImage);
		displayContent = new ArrayList<String>();
		imagesList = db.GetAllImages();
		//System.out.println("Hell: " + imagesList.size());
		db.close();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queued_image_list);
		InitializeComponant();
		
		for (Images_DB anImages_DB : imagesList) {
			//System.out.println("Time: " + anImages_DB.GetDateTime());
			displayContent.add("Serial Number: " +anImages_DB.GetId() +"\nDoctor Id: "+ anImages_DB.GetDoctorId());
		}
		
		ArrayAdapter<String> displayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayContent);
		aListView.setAdapter(displayAdapter);
		
		aListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				Images_DB myImage = imagesList.get(position);
				
				Intent img = new Intent(QueuedImageList.this, QueuedImage.class);
				img.putExtra("getId", myImage.GetId());
				img.putExtra("getDoctorId", myImage.GetDoctorId());
				img.putExtra("getImageLocation", myImage.GetCapturedImage());
				img.putExtra("getDescription", myImage.GetDescription());
				img.putExtra("getDateTaken", myImage.GetDateTime());
				img.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				img.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(img);	
				finish();
			}		
			
		});
	}
	
}
