package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
	private Long id;
	private String productName;
	private BigDecimal unitPrice;
	private int quantity;
	private BigDecimal totalPrice;

	public OrderItemDTO(Long id, String productName, BigDecimal productPrice, Integer quantity, BigDecimal totalPrice) {
		this.id = id;
		this.productName = productName;
		this.unitPrice = productPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
}