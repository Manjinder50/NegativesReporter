package com.practice.NegativesReporter.model;

public class Order {

	private Long orderId;
	private String name;
	private Long templateId;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", name=" + name + ", templateId=" + templateId + "]";
	}
}
