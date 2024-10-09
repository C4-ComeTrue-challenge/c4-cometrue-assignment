package org.c4marathon.assignment.order.ui.dto.request;

public record AddToCartRequest(
	Long productId,
	int quantity,
	Integer version
) {
}
