package com.order.mapper;

import com.order.dto.OrderDTO;
import com.order.dto.OrderItemDTO;
import com.order.dto.ProductMessage;
import com.order.model.OrderItem;
import com.order.model.Orders;
import com.order.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

	public OrderItem convertToOrderItem(Product product, Orders order, ProductMessage productMsg) {
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setOrder(order);
		item.setUnitPrice(product.getPrice());
		item.setQuantity(productMsg.getQuantity());
		item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(productMsg.getQuantity())));
		return item;
	}

	public OrderDTO convertToDTO(Orders order) {
		List<OrderItemDTO> itemsDTO = order.getItems().stream()
				.map(item -> new OrderItemDTO(
						item.getId(),
						item.getProduct().getName(),
						item.getProduct().getPrice(),
						item.getQuantity(),
						item.getTotalPrice()
				))
				.collect(Collectors.toList());

		return new OrderDTO(order.getId(), order.getStatus(), order.getTotalPrice(), itemsDTO);
	}

	public BigDecimal calculateTotalPrice(Orders order) {
		return order.getItems().stream()
				.map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
