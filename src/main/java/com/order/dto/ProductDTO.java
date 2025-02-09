package com.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductDTO {
	private Long id;
	private String name;
	private BigDecimal price;

	public ProductDTO() {}

	public ProductDTO(Long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

}
