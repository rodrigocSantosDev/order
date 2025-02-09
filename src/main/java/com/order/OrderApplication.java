package com.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.order",
		"com.order.security"
})
@EntityScan(basePackages = {
		"com.order.model",
		"com.order.security.model"
})
@EnableJpaRepositories(basePackages = {
		"com.order.repository",
		"com.order.security.repository"
})
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
