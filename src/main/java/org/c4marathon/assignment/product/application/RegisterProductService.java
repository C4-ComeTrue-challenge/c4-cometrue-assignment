package org.c4marathon.assignment.product.application;

import java.util.UUID;

import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterProductService {
	private final ProductRepository productRepository;

	public RegisterProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Long register(Command cmd) {
		Product newProduct = new Product(cmd.name(), cmd.description(), cmd.price(), cmd.stock());
		Product savedProduct = productRepository.save(newProduct);
		return savedProduct.getId();
	}

	public record Command(
		UUID sellerId,
		String name,
		String description,
		Long price,
		Long stock
	) {
	}
}
