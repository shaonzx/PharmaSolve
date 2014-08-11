package com.mti.pharmasolve;

import java.io.File;

import com.mti.pharmasolve.adapters.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QueuedImage extends Activity {
	
	private Button btnUpload, btnDiscard;
	
	private int id;
	private String doctorId;
	private String imageLocation;
	private String description;
	private String takenAt;
	
	private ImageView anImageView;
	private TextView txtDoctorId, txtTakenAt, txtDescription;
	
	private Uri imageUri;
	
	private Bitmap bitmap;
	
	
	
	
	private void InitializeEverything()
	{
		
		btnDiscard = (Button) findViewById(R.id.queued_image_btnDiscard);
		btnUpload = (Button) findViewById(R.id.queued_image_btnUpload);
		
		
		anImageView = (ImageView) findViewById(R.id.queued_image_imgPreview);
		
		txtDoctorId = (TextView) findViewById(R.id.queued_image_txtDoctorId);
		txtTakenAt =(TextView) findViewById(R.id.queued_image_txtTakenAt);
		txtDescription = (TextView) findViewById(R.id.queued_image_txtDescription);
		
		id = getIntent().getExtras().getInt("getId");	
		doctorId = getIntent().getExtras().getString("getDoctorId");
		imageLocation = getIntent().getExtras().getString("getImageLocation");
		description = getIntent().getExtras().getString("getDescription");
		takenAt = getIntent().getExtras().getString("getDateTaken");
		
		imageUri = Uri.parse(imageLocation);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		
		bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
				
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queued_image);
		InitializeEverything();	
		
		
		anImageView.setImageBitmap(bitmap);
		txtDoctorId.setText("Doctor Id: " + doctorId +"\n");
		txtTakenAt.setText("Taken At: " + takenAt +"\n");
		txtDescription.setText("Description: " + "\n" + description);
		
		btnDiscard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*File file = new File(imageUri.toString());
				boolean deleted = file.delete();*/
				
				File fdelete = new File(imageUri.getPath());
				
				if (fdelete.exists())
				{
					fdelete.delete();
					//fdelete.
					Toast.makeText(QueuedImage.this, "Image Deleted!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(QueuedImage.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
				}
				
			
				
				DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				int something = db.DeleteImage(id);
				Toast.makeText(QueuedImage.this, something +" rows deleted!", Toast.LENGTH_SHORT).show();
				
				
				Intent img = new Intent(QueuedImage.this, Dashboard.class);
				img.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				img.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(img);	
				finish();
				
				
			}
		});		
		
		
		
	}	
}
