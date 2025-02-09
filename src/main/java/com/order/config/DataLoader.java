package com.order.config;


import com.order.model.Product;
import com.order.repository.ProductRepository;
import com.order.security.model.User;
import com.order.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public DataLoader(ProductRepository productRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (productRepository.count() == 0) {
			List<Product> products = List.of(
					new Product(null, "Notebook Dell XPS 13", "Ultrabook com processador Intel Core i7 e 16GB RAM", new BigDecimal("8999.99")),
					new Product(null, "Mouse Logitech MX Master 3", "Mouse sem fio ergonômico com precisão avançada", new BigDecimal("499.90")),
					new Product(null, "Teclado Mecânico Keychron K2", "Teclado mecânico sem fio com switches Gateron", new BigDecimal("799.00")),
					new Product(null, "Monitor LG UltraWide 34\"", "Monitor 34 polegadas ultrawide Full HD", new BigDecimal("2599.90")),
					new Product(null, "Cadeira Gamer DXRacer", "Cadeira ergonômica para jogos e escritório", new BigDecimal("1899.99"))
			);

			productRepository.saveAll(products);
		}

		if (userRepository.count() == 0) {
			List<User> users = List.of(
					new User(null, "admin", passwordEncoder.encode("admin123")),
					new User(null, "user", passwordEncoder.encode("user123"))
			);

			userRepository.saveAll(users);
		}
	}
}
