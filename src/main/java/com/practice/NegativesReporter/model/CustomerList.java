package com.practice.NegativesReporter.model;

import java.io.Serializable;
import java.util.List;



public class CustomerList{

	private List<Customer> customers;

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public String toString() {
		return "CustomerList [customers=" + customers + "]";
	}
}
