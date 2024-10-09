package org.c4marathon.assignment.order.domain;

import java.io.Serializable;
import java.util.UUID;

public class Cart implements Serializable {
	private UUID customerId;

	public Cart(UUID customerId) {
		this.customerId = customerId;
	}

	public void add(Long productId, int quantity) {

	}
}
