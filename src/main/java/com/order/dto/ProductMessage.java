package com.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductMessage {
	private Long id;
	private String name;
	private int quantity;

	public ProductMessage() {}

	public ProductMessage(Long id, String name, int quantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
	}

}

