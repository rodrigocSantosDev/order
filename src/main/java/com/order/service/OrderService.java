package com.order.service;

import com.order.dto.OrderDTO;
import com.order.dto.OrderMessage;
import com.order.exception.OrderProcessingException;
import com.order.exception.ProductNotFoundException;
import com.order.mapper.OrderMapper;
import com.order.model.OrderItem;
import com.order.model.Orders;
import com.order.model.Product;
import com.order.repository.OrderRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final MeterRegistry registry;
	private final OrderMapper orderMapper;

	public OrderService(OrderRepository orderRepository, ProductService productService, MeterRegistry registry, OrderMapper orderMapper) {
		this.orderRepository = orderRepository;
		this.productService = productService;
		this.registry = registry;
		this.orderMapper = orderMapper;
	}

	public void processOrder(OrderMessage orderMessage) {
		log.info("Processando pedido...");

		try {
			Orders order = convertToEntity(orderMessage);
			saveOrder(order);
			log.info("Pedido ID: {} processado com sucesso", order.getId());
		} catch (Exception e) {
			log.error("Erro ao processar pedido", e);
			throw new OrderProcessingException("Erro ao processar pedido", e);
		}
	}

	private Orders convertToEntity(OrderMessage orderMessage) {
		Orders order = new Orders();
		order.setStatus(orderMessage.getStatus());
		log.info("Convertendo mensagem para entidade Order...");

		List<OrderItem> items = orderMessage.getItems().stream()
				.map(productMsg -> {
					Product product = productService.getProductById(productMsg.getId());
					if (product == null) {
						throw new ProductNotFoundException("Produto com ID " + productMsg.getId() + " não encontrado.");
					}
					return orderMapper.convertToOrderItem(product, order, productMsg);
				}).collect(Collectors.toList());

		order.setItems(items);
		log.info("Itens convertidos e atribuídos ao pedido.");
		return order;
	}

	public Orders saveOrder(Orders order) {
		try {
			BigDecimal totalPrice = orderMapper.calculateTotalPrice(order);
			order.setTotalPrice(totalPrice);

			log.info("Salvando pedido ID: {} com total de {}", order.getId(), totalPrice);
			Orders savedOrder = orderRepository.save(order);

			Counter orderCounter = registry.counter("orders.created", "status", order.getStatus());
			orderCounter.increment();

			log.info("Pedido ID: {} salvo com sucesso.", savedOrder.getId());
			return savedOrder;

		} catch (Exception e) {
			log.error("Erro ao salvar pedido", e);
			throw new OrderProcessingException("Erro ao salvar pedido", e);
		}
	}

	public Page<OrderDTO> getAllOrders(int page, int size) {
		log.info("Buscando todos os pedidos, página {} de tamanho {}", page, size);
		Page<Orders> orderPage = orderRepository.findAll(PageRequest.of(page, size));
		return orderPage.map(orderMapper::convertToDTO);
	}

	public Optional<OrderDTO> getOrderById(Long id) {
		log.info("Buscando pedido com ID: {}", id);
		return orderRepository.findById(id).map(orderMapper::convertToDTO);
	}
}
