package org.c4marathon.assignment.order.domain;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
	Optional<Cart> findByCustomerId(UUID customerId);

	Cart save(Cart cart);
}
