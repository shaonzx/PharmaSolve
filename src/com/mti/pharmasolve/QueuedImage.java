package com.mti.pharmasolve;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
		txtDoctorId.setText("Doctor Id: " + doctorId);
		txtTakenAt.setText("Taken At: " + takenAt);
		txtDescription.setText("Description: " + "\n" + description);
		
	}	
}
