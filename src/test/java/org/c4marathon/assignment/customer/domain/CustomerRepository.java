package org.c4marathon.assignment.customer.domain;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
	Customer save(Customer customer);

	Optional<Customer> findById(UUID id);
}
