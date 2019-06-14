package com.practice.NegativesReporter.model;

import java.util.List;

public class Orders {

	private List<Order> orders;

	private PaginationMetaData metadata;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public PaginationMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(PaginationMetaData metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "Orders [orders=" + orders + ", metadata=" + metadata + "]";
	}

}
