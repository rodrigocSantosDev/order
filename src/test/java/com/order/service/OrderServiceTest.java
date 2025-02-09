package com.order.service;

import com.order.dto.OrderMessage;
import com.order.dto.ProductMessage;
import com.order.exception.OrderProcessingException;
import com.order.mapper.OrderMapper;
import com.order.model.Orders;
import com.order.model.OrderItem;
import com.order.model.Product;
import com.order.repository.OrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductService productService;

	@Mock
	private MeterRegistry registry;

	@Mock
	private OrderMapper orderMapper;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void processOrder_shouldSaveOrderSuccessfully() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Product A");
		product.setPrice(new BigDecimal("100.00"));

		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setStatus("NEW");
		orderMessage.setItems(Arrays.asList(
				new ProductMessage(1L, "Product A", 2)
		));

		Counter orderCounter = mock(Counter.class);
		when(registry.counter(anyString(), anyString(), anyString())).thenReturn(orderCounter);

		when(productService.getProductById(1L)).thenReturn(product);
		when(orderRepository.save(any(Orders.class))).thenReturn(new Orders());

		orderService.processOrder(orderMessage);

		verify(orderRepository, times(1)).save(any(Orders.class));
		verify(registry, times(1)).counter("orders.created", "status", "NEW");
	}

	@Test
	void processOrder_shouldThrowProductNotFoundException_whenProductNotFound() {
		OrderMessage orderMessage = new OrderMessage();
		orderMessage.setStatus("NEW");
		orderMessage.setItems(Arrays.asList(
				new ProductMessage(999L, "Nonexistent Product", 2)
		));

		OrderProcessingException exception = assertThrows(OrderProcessingException.class, () -> {
			orderService.processOrder(orderMessage);
		});

		assertEquals("Erro ao processar pedido", exception.getMessage());
	}

	@Test
	void saveOrder_shouldCalculateTotalPriceCorrectly() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Product A");
		product.setPrice(new BigDecimal("100.00"));

		Orders order = new Orders();
		order.setStatus("NEW");
		order.setItems(Arrays.asList(
				new OrderItem(null, order, product, 2, product.getPrice(), product.getPrice().multiply(BigDecimal.valueOf(2)))));

		BigDecimal expectedTotalPrice = new BigDecimal("200.00");

		when(orderMapper.calculateTotalPrice(order)).thenReturn(expectedTotalPrice);
		when(orderRepository.save(any(Orders.class))).thenReturn(order);

		Counter orderCounter = mock(Counter.class);
		when(registry.counter(anyString(), anyString(), anyString())).thenReturn(orderCounter);

		Orders savedOrder = orderService.saveOrder(order);

		assertEquals(expectedTotalPrice, savedOrder.getTotalPrice());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void saveOrder_shouldThrowOrderProcessingException_whenErrorOccurs() {
		Orders order = new Orders();
		order.setStatus("NEW");

		when(orderRepository.save(any(Orders.class))).thenThrow(new RuntimeException("Error saving order"));

		OrderProcessingException exception = assertThrows(OrderProcessingException.class, () -> {
			orderService.saveOrder(order);
		});

		assertEquals("Erro ao salvar pedido", exception.getMessage());
	}
}