package com.mti.pharmasolve;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Images_DB;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TakePicture extends Activity {



	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	// private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	// public static final int MEDIA_TYPE_VIDEO = 2;

	// directory name to store captured images
	private static final String IMAGE_DIRECTORY_NAME = ".pharmasolve_images";

	private Uri fileUri = Uri.parse(""); // file url to store image/video

	private ImageView imgPreview;
	private Button btnStore, btnDiscard;
	EditText edtDescription;
	Spinner spnrDoctorId;

	private Drawable myDrawable;

	DatabaseHelper db;

	List<String> GalleryList;

	List<String> doctorList;

	private void InitializeComponant() {
		GalleryList = new ArrayList<String>();
		imgPreview = (ImageView) findViewById(R.id.take_picture_imgPreview);
		btnStore = (Button) findViewById(R.id.take_picture_btnStore);
		btnDiscard = (Button) findViewById(R.id.take_picture_btnDiscard);
		
		edtDescription = (EditText) findViewById(R.id.take_picture_edtDescription);
		spnrDoctorId = (Spinner) findViewById(R.id.take_picture_spnrDoctorId);
		
		doctorList = new ArrayList<String>();
		doctorList.add("<Select>");
		doctorList.add("Doc1");
		doctorList.add("Doc2");
		doctorList.add("Doc3");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);
		InitializeComponant();
		
		SetDocListAdapter();

		CaptureImage();

		btnStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// CaptureImage();
				
				if(spnrDoctorId.getSelectedItem().equals("<Select>"))
				{
					Toast.makeText(TakePicture.this, "Select a Doctor Id First!", Toast.LENGTH_SHORT).show();
				}
				else
				{

					Images_DB anImages_DB = new Images_DB(spnrDoctorId.getSelectedItem().toString(), fileUri
							.toString(), edtDescription.getText().toString());

					db = new DatabaseHelper(getApplicationContext());
					long something = db.InsertImage(anImages_DB);
					db.close();
					Toast.makeText(TakePicture.this,
							"Affected Row: " + something, Toast.LENGTH_LONG)
							.show();
					
					Intent startDashboard = new Intent(TakePicture.this,
							Dashboard.class);					
					startDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(startDashboard);
					finish();
				}
			}
		});

		btnDiscard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				File file = new File(fileUri.getPath());
				boolean deleted = file.delete();

				if (deleted) {
					Intent startDashboard = new Intent(TakePicture.this,
							Dashboard.class);
					Toast.makeText(TakePicture.this,
							"Discarded", Toast.LENGTH_SHORT)
							.show();
					startDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(startDashboard);
					finish();
				} else {
					Toast.makeText(TakePicture.this,
							deleted + fileUri.toString(), Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
	}

	private void SetDocListAdapter() {
		// TODO Auto-generated method stub
		ArrayAdapter<String> doctorlistAdapter = new ArrayAdapter<String>(TakePicture.this, android.R.layout.simple_list_item_1, doctorList);
		doctorlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spnrDoctorId.setAdapter(doctorlistAdapter);
	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;

		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");

			Uri myUri = Uri.fromFile(mediaFile);

			/************************ Practice ****************/

			/************************************************/

		} else {
			return null;
		}

		return mediaFile;
	}

	private void CaptureImage() {
		Intent imgCapturingAction = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		

		imgCapturingAction.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);	

		// start the image capture Intent
		startActivityForResult(imgCapturingAction,
				CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				previewCapturedImage();
				
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Display image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			imgPreview.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			imgPreview.setImageBitmap(bitmap);

			StoreBitMapToSdCard(bitmap);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void StoreBitMapToSdCard(Bitmap bitmap) {

		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);
		if (!mediaStorageDir.exists()) {
			mediaStorageDir.mkdir();
		}

		String directory = mediaStorageDir.getAbsolutePath();
		File aFile = new File(fileUri.toString());
		String fileName = aFile.getName();

		// Toast.makeText(this, "Name: " + fileName + "\nPath: " + directory,
		// Toast.LENGTH_LONG).show();
		// Delete existing file before writing new one
		File fdelete = new File(fileUri.toString());

		if (fdelete.exists())
			fdelete.delete();

		// Write new Reduced File
		File f = new File(directory, fileName);

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}