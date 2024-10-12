package org.c4marathon.assignment.order.domain.vo;

public class CartItem {
	private final Long productId;
	private final int quantity;

	public CartItem(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public CartItem increaseQuantity(int increment) {
		return new CartItem(productId, quantity + increment);
	}
}
