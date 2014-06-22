package com.mti.pharmasolve.model;

public class Customers_DB {
	
	int id;
	String customerId;
	
	//Constructors	
	public Customers_DB(){		
	}
	
	public Customers_DB(String customerId){		
		this.customerId = customerId;
	}
	
	//Setter Methods	
//	public void SetId(int id) {
//        this.id = id;
//    }
	public void SetCustomerId(String customerId){
		this.customerId = customerId;
	}
	
	//Getter Methods
//	public int GetId(){
//		return this.id;
//	}
	public String GetCustomerId(){
		return this.customerId;
	}

}
