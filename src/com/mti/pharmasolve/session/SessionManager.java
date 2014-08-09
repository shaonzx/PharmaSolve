package com.mti.pharmasolve.session;

import com.mti.pharmasolve.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	SharedPreferences pref;
	Editor editor;
	Context _context;
	
	int PRIVATE_MODE = 0;
	
	static final String PREF_NAME = "pharmaSolve";
	static final String IS_LOGIN = "isLoggedIn";
	static final String KEY_USER_ID = "userId";
	static final String SERVICE_STATUS = "isServiceStarted";
	
	
	public SessionManager(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	public void CreateLoginSession(String userId)
	{		
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
	}
	
	public String GetUserIdFromSharedPreferences()
	{
		String userId = pref.getString(KEY_USER_ID, null);
		return userId;
	}
	
	public boolean IsLoggedIn()
	{
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	public void LogoutUser()
	{
		// Clear All SharedPreferences Data
		editor.clear();
		editor.commit();
		
		// After Logout Redirect User to Login Screen
		Intent i = new Intent(_context, Login.class);
		
		// Closing All the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// New Flag to Start New Activity
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Start Activity to Show the Login Screen
		_context.startActivity(i);
	}

	public void FlagServiceStart()
	{
		editor.putBoolean(SERVICE_STATUS, true);
		editor.commit();
	}
	
	public boolean IsServiceStarted()
	{
		return pref.getBoolean(SERVICE_STATUS, false);
	}
}
