package com.practice.NegativesReporter.model;

public class PaginationMetaData {

	private long limit;

	private long start;

	private long total;

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "PaginationMetaData [limit=" + limit + ", start=" + start + ", total=" + total + "]";
	}
}
