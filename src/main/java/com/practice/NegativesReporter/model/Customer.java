package com.practice.NegativesReporter.model;

import java.util.List;

public class Customer {

	private Long id;
	private String name;
	private List<Supplier> suppliers;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Supplier> getSuppliers() {
		return suppliers;
	}
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", suppliers=" + suppliers + "]";
	} 
}
