package com.order.dto;

import java.util.List;

public class OrderMessage {
	private String status;
	private List<ProductMessage> items;

	public OrderMessage() {}

	public OrderMessage(String status, List<ProductMessage> items) {
		this.status = status;
		this.items = items;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductMessage> getItems() {
		return items;
	}

	public void setItems(List<ProductMessage> items) {
		this.items = items;
	}
}