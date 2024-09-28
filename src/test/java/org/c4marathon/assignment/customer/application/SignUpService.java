package org.c4marathon.assignment.customer.application;

import java.util.UUID;

import org.c4marathon.assignment.customer.domain.Customer;
import org.c4marathon.assignment.customer.domain.CustomerRepository;
import org.c4marathon.assignment.customer.domain.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignUpService {
	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;

	public SignUpService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UUID register(Command cmd) {
		Customer newCustomer = customerRepository.save(
			new Customer(cmd.email(), cmd.password(), cmd.name(), passwordEncoder));
		return newCustomer.getId();
	}

	public record Command(
		String email,
		String password,
		String name
	) {
	}
}
