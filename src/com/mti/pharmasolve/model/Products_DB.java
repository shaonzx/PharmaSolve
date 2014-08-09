package com.mti.pharmasolve.model;

public class Products_DB {
	
	String customerId;
	String productName;
	int productQuantity;
	double salesPrice;
	String productId;
	
	//Constructors
	public Products_DB(){
		
	}

	public Products_DB(String customerId, String productName, int productQuantity, double salesPrice, String productId){
		
		this.customerId = customerId;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.salesPrice = salesPrice;
		this.productId = productId;
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
	public void SetSalesPrice(double salesPrice)
	{
		this.salesPrice = salesPrice;
	}
	public void SetProductId(String productId)
	{
		this.productId = productId;
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
	public double GetSalesPrice()
	{
		return this.salesPrice;
	}
	public String GetProductId()
	{
		return this.productId;
	}

}
