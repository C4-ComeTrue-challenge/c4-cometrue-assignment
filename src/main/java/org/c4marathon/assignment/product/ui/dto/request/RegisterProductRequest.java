package org.c4marathon.assignment.product.ui.dto.request;

public record RegisterProductRequest(
	String name,
	String description,
	Long price,
	Long stock
) {
}
