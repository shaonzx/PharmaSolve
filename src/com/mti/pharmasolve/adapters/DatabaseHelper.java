package com.mti.pharmasolve.adapters;

import java.util.ArrayList;
import java.util.List;

import com.mti.pharmasolve.model.Customers_DB;
import com.mti.pharmasolve.model.Products_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
    // Database Version
    private static final int DATABASE_VERSION = 1; 
    // Database Name
    private static final String DATABASE_NAME = "productsManager";
    
    // Table Names
    private static final String TABLE_CUSTOMERS = "Customers";
    private static final String TABLE_PRODUCTS = "Products";
    
    // Common column names
    private static final String CUSTOMER_ID = "customerId";  //Only Column in Customers Table
    
    // PRODUCTs Table - column nmaes
    private static final String PRODUCTS_NAME = "productName";
    private static final String PRODUCTS_QUANTITY = "quantity";
    
    // Table Create Statements
    // CUSTOMER table create statement
    private static final String CREATE_TABLE_CUSTOMERS = "CREATE TABLE "
            + TABLE_CUSTOMERS + "(" + CUSTOMER_ID + " TEXT PRIMARY KEY NOT NULL" + ")";
 
    // PRODUCTS table create statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS
            + "(" + CUSTOMER_ID + " TEXT," + PRODUCTS_NAME + " TEXT,"
            + PRODUCTS_QUANTITY + " INTEGER" + ")";
    
    
    
	
	public DatabaseHelper(Context applicationContext) {		
		super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(CREATE_TABLE_CUSTOMERS);
		db.execSQL(CREATE_TABLE_PRODUCTS);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        
        onCreate(db);
		
	}
	
	public long InsertCustomer(Customers_DB aCustomers_DB)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();	    
	    values.put(CUSTOMER_ID, aCustomers_DB.GetCustomerId());	    
	    long something = db.insert(TABLE_CUSTOMERS, null, values);	    
	    return something;	
	}
	
	public long InsertProduct(Products_DB aProducts_DB)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();	
		values.put(CUSTOMER_ID, aProducts_DB.GetCustomerId());
		values.put(PRODUCTS_NAME, aProducts_DB.GetProductName());
		values.put(PRODUCTS_QUANTITY, aProducts_DB.GetProductQuantity());
		
		long something = db.insert(TABLE_PRODUCTS, null, values);
		
		return something;		
	}
	
	
	/*
	 * getting all Products from Products Table
	 * */
	public List<Products_DB> GetProducts(String string)
	{
		List<Products_DB> productList = new ArrayList<Products_DB>();
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS + " WHERE " + CUSTOMER_ID + " LIKE " + "'" + string+ "'";
		
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    if (c.moveToFirst()) {
	        do {
	            Products_DB aProducts_DB = new Products_DB();
	            
	            aProducts_DB.SetCustomerId(c.getString(c.getColumnIndex(CUSTOMER_ID)));
	            aProducts_DB.SetProductName(c.getString(c.getColumnIndex(PRODUCTS_NAME)));
	            aProducts_DB.SetProductQuantity(c.getInt(c.getColumnIndex(PRODUCTS_QUANTITY)));
	            
	            // adding to products list
	            productList.add(aProducts_DB);
	            
	        } while (c.moveToNext());
	    }
	    
	    return productList;
		
	}
	
	
	
	
	
	/*
	 * getting all Customer ID's from Customers Table
	 * */
	public List<Customers_DB> getAllCustomers() {
	    List<Customers_DB> customerList = new ArrayList<Customers_DB>();
	    String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;
	 
	    //Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            Customers_DB aCustomers_DB = new Customers_DB();
	            
//	            aCustomers_DB.setId(c.getInt((c.getColumnIndex(KEY_ID))));
//	            aCustomers_DB.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
//	            aCustomers_DB.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));	            
	            aCustomers_DB.SetCustomerId(c.getString(c.getColumnIndex(CUSTOMER_ID)));
	 
	            // adding to customer list
	            customerList.add(aCustomers_DB);
	        } while (c.moveToNext());
	    }
	 
	    return customerList;
	}
	
	public void deleteCustomer(String string)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_CUSTOMERS, CUSTOMER_ID + " = ?",
	            new String[] {string});
	}
	
	public void deleteProducts(String string)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + CUSTOMER_ID + " LIKE " + "'" + string+ "'";
		db.execSQL(query);
	}
	
	
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
