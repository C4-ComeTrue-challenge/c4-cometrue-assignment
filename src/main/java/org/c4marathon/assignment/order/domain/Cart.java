package org.c4marathon.assignment.order.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.order.domain.vo.CartItem;

public class Cart implements Serializable {
	private UUID customerId;
	private List<CartItem> items = new ArrayList<>();

	public Cart(UUID customerId) {
		this.customerId = customerId;
	}

	public void add(Long productId, int quantity) {
		Optional<CartItem> findCartItem = findCartItemByProductId(productId);
		if (findCartItem.isPresent()) {
			CartItem newCartItem = findCartItem.get().increaseQuantity(quantity);
			items.set(items.indexOf(findCartItem.get()), newCartItem);
		} else {
			items.add(new CartItem(productId, quantity));
		}
	}

	private Optional<CartItem> findCartItemByProductId(Long productId) {
		return items.stream()
			.filter(cartItem -> cartItem.getProductId().equals(productId))
			.findFirst();
	}
}
