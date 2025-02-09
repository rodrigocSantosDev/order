package com.order.service;

import com.order.dto.ProductDTO;
import com.order.model.Product;
import com.order.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll()
				.stream()
				.map(product -> new ProductDTO(product.getId(), product.getName(), product.getPrice()))
				.collect(Collectors.toList());
	}
}