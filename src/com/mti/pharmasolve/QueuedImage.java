package com.mti.pharmasolve;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.session.SessionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QueuedImage extends Activity {
	
	ProgressDialog aProgressDialog;
	
	private Button btnUpload, btnDiscard;
	
	private int id;
	private String doctorId;
	private String imageLocation;
	private String description;
	private String takenAt;
	static String imageString;
	
	private ImageView anImageView;
	private TextView txtDoctorId, txtTakenAt, txtDescription;
	
	private Uri imageUri;
	
	private Bitmap bitmap;
	
	String BitmapToBase64StringConvertion(Bitmap myBitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] data = baos.toByteArray();
		
		String someString = Base64.encodeToString(data, Base64.DEFAULT);
		
		return someString;
	}
	
	
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
		
		//imageString = BitmapToBase64StringConvertion(bitmap);
				
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
		
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new UploadImage().execute();
				

				
				//Toast.makeText(QueuedImage.this, "Base65:" + image2Base64, Toast.LENGTH_LONG).show();
				
//				Intent img = new Intent(QueuedImage.this, Dashboard.class);
//				img.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				img.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(img);	
//				finish();
				
			}
		});
		
	}
	
	class UploadImage extends AsyncTask<Void, Void, Void>{

		JSONObject aJSONObject = new JSONObject();
		String output = "";
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			aProgressDialog = new ProgressDialog(QueuedImage.this);
			aProgressDialog.setMessage("Uploading Image...");
			aProgressDialog.setIndeterminate(true);
			aProgressDialog.setCancelable(false);
			//aProgressDialog.setCanceledOnTouchOutside(true);
			aProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			SessionManager aSession = new SessionManager(getApplicationContext());
			String userId = aSession.GetUserIdFromSharedPreferences();
			
			RestAPI api = new RestAPI();
			try {
				
				Uri myUri = Uri.parse(imageLocation);
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 0;				
				Bitmap myBitmap = BitmapFactory.decodeFile(myUri.getPath(), options);				
				String image2Base64 = BitmapToBase64StringConvertion(myBitmap);
				
				aJSONObject = api.InsertImages(userId, doctorId, description, takenAt, image2Base64);
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
			Toast.makeText(QueuedImage.this, aJSONObject.toString(), Toast.LENGTH_LONG).show();
			
			System.out.println("Base String: " + imageString);
			
			try {
				output = aJSONObject.getString("Value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(output.equals("success"))
			{
				
				File fdelete = new File(imageUri.getPath());
				
				if (fdelete.exists())
				{
					fdelete.delete();					
				}
				else
				{
					Toast.makeText(QueuedImage.this, "Unable to remove from SD Card", Toast.LENGTH_SHORT).show();
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
			else
			{
				Toast.makeText(QueuedImage.this, "Something Went Wrong,  Please Try Again", Toast.LENGTH_SHORT).show();
			}
		}	
				
	}
}
