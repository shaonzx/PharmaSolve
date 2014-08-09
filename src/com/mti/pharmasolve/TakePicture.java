package com.mti.pharmasolve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.mti.pharmasolve.adapters.DatabaseHelper;
import com.mti.pharmasolve.model.Images_DB;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePicture extends Activity {
	
	
	private String doctorId = "C007";
	private String description = "Some Description";

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    //private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
   // public static final int MEDIA_TYPE_VIDEO = 2;
    
    // directory name to store captured images
    private static final String IMAGE_DIRECTORY_NAME = ".pharmasolve_images";
    
    private Uri fileUri = Uri.parse(""); // file url to store image/video
    
    private ImageView imgPreview;
    private Button btnStore, btnDiscard;
    
    private Drawable myDrawable;
    
    DatabaseHelper db;
    
    private void InitializeComponant()
    {
    	
    	imgPreview = (ImageView) findViewById(R.id.take_picture_imgPreview);
    	btnStore = (Button) findViewById(R.id.take_picture_btnStore);
    	btnDiscard = (Button) findViewById(R.id.take_picture_btnDiscard);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);
		InitializeComponant();

		CaptureImage();

		btnStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//CaptureImage();				
				
				Images_DB anImages_DB = new Images_DB(doctorId, fileUri.toString(), description);
				
				db = new DatabaseHelper(getApplicationContext());				
				long something = db.InsertImage(anImages_DB);
				db.close();
				Toast.makeText(TakePicture.this, "Affected Row: " +something, Toast.LENGTH_LONG).show();
			}
		});
		
		btnDiscard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				File file = new File(fileUri.toString());
				boolean deleted = file.delete();
				
				if(deleted)
				{
					Intent startDashboard = new Intent(TakePicture.this, Dashboard.class);
					Toast.makeText(TakePicture.this, deleted + fileUri.toString(), Toast.LENGTH_SHORT).show();
					startActivity(startDashboard);
				}
				else
				{
					Toast.makeText(TakePicture.this, deleted + fileUri.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}

	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

	    // External sdcard location
	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);

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

	    } else {
	        return null;
	    }

	    return mediaFile;
	}

	private void CaptureImage()
	{
		Intent imgCapturingAction = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        imgCapturingAction.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        
        // start the image capture Intent
        startActivityForResult(imgCapturingAction, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}