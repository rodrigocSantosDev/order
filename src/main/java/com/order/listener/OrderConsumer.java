package com.order.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.OrderMessage;
import com.order.exception.OrderProcessingException;
import com.order.exception.ProductNotFoundException;
import com.order.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OrderConsumer {

	private final OrderService orderService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public OrderConsumer(OrderService orderService) {
		this.orderService = orderService;
	}

	@RabbitListener(queues = "orderQueue")
	public void receiveOrder(String message) {
		try {
			OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
			orderService.processOrder(orderMessage);
		} catch (JsonProcessingException e) {
			log.error("Erro ao converter JSON: {}", e.getMessage(), e);
		} catch (ProductNotFoundException e) {
			log.error("Erro: Produto não encontrado - {}", e.getMessage(), e);
		} catch (OrderProcessingException e) {
			log.error("Erro no processamento do pedido: {}", e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erro inesperado ao processar mensagem: {}", e.getMessage(), e);
		}
	}
}
