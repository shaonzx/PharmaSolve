package com.mti.pharmasolve;

public class Products {	
	
	private String productName;
	private String productCode;
	private double salesPrice;
	
	public Products(String aProductName, String aProductCode, double aSalesPrice)
	{
		
		productName = aProductName;
		productCode = aProductCode;
		salesPrice = aSalesPrice;
	}
	
	public String GetProductName()
	{
		return productName;
	}
	public String GetProductCode()
	{
		return productCode;
	}
	public double GetSalesPrice()
	{
		return salesPrice;
	}

}
