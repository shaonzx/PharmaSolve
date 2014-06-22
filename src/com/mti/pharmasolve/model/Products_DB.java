package com.mti.pharmasolve.model;

public class Products_DB {
	
	String customerId;
	String productName;
	int productQuantity;
	
	//Constructors
	public Products_DB(){
		
	}

	public Products_DB(String customerId, String productName, int productQuantity){
		
		this.customerId = customerId;
		this.productName = productName;
		this.productQuantity = productQuantity;
	}
	
	
	//SetterMethods
	public void SetCustomerId(String customerId){
		this.customerId = customerId;
	}
	public void SetProductName(String productName){
		this.productName = productName;
	}
	public void SetProductQuantity(int productQuantity){
		this.productQuantity = productQuantity;
	}
	
	
	//GetterMethods
	public String GetCustomerId(){
		return this.customerId;
	}
	public String GetProductName(){
		return this.productName;
	}
	public int GetProductQuantity(){
		return this.productQuantity;
	}

}
