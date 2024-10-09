package org.c4marathon.assignment.order.application;

import java.util.UUID;

import org.c4marathon.assignment.order.domain.Cart;
import org.c4marathon.assignment.order.domain.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class AddToCartService {
	private final CartRepository cartRepository;

	public AddToCartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public void add(Command cmd) {
		Cart cart = cartRepository.findByCustomerId(cmd.customerId())
			.orElseGet(() -> new Cart(cmd.customerId()));
		cart.add(cmd.productId(), cmd.quantity());
		cartRepository.save(cart);
	}

	public record Command(
		UUID customerId,
		Long productId,
		int quantity,
		Integer version
	) {
	}
}
