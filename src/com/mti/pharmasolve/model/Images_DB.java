package com.mti.pharmasolve.model;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;

public class Images_DB {
	
	private int id;	
	private String doctorId;
	private String capturedImage;
	private String description;
	private String strDate;
		
	//Constructors
	public Images_DB()
	{		
	}
	
	public Images_DB(String doctorId, String capturedImage, String description)
	{
		this.doctorId = doctorId;
		this.capturedImage = capturedImage;
		this.description = description;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	    Date date = new Date();
	    
	    this.strDate = dateFormat.format(date);		
	} 
	
	
	//Setters
	public void SetId(int id)
	{
		this.id = id;
	}
	public void SetDoctorId(String doctorId)
	{
		this.doctorId = doctorId;
	}
	public void SetCapturedImage(String bs)
	{
		this.capturedImage = bs;
	}
	public void SetDescription(String description)
	{
		this.description = description;
	}
	public void SetDateTime(String date) 
	{        
        strDate = date;
	}
	
	
	//Getters
	public int GetId()
	{
		return id;
	}
	public String GetDoctorId()
	{
		return doctorId;
	}
	
	public String GetCapturedImage()
	{
		return capturedImage;
	}
	
	public String GetDescription()
	{
		return description;
	}
	
/*	public String GetDateTime() 
	{
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
	}*/
	
	
	//DB stored image with a boolean value....
	public String GetDateTime()
	{
		return strDate;
	}

}
