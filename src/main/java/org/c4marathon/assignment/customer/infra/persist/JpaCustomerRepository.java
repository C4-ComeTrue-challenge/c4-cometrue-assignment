package org.c4marathon.assignment.customer.infra.persist;

import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.customer.domain.Customer;
import org.c4marathon.assignment.customer.domain.CustomerRepository;
import org.c4marathon.assignment.customer.infra.persist.datajpa.JpaCustomerRecordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCustomerRepository implements CustomerRepository {
	private final JpaCustomerRecordRepository repository;

	public JpaCustomerRepository(JpaCustomerRecordRepository jpaCustomerRecordRepository) {
		this.repository = jpaCustomerRecordRepository;
	}

	@Override
	public Customer save(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public Optional<Customer> findById(UUID id) {
		return repository.findById(id);
	}
}
