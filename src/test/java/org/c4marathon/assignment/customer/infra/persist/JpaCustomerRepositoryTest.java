package org.c4marathon.assignment.customer.infra.persist;

import org.c4marathon.assignment.customer.domain.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaCustomerRepository.class})
class JpaCustomerRepositoryTest {
	@Autowired
	private JpaCustomerRepository jpaCustomerRepository;

	@Test
	void save() {
		Customer newCustomer = new Customer(null, "newEmail", "newPassword", "newName");
		Customer savedCustomer = jpaCustomerRepository.save(newCustomer);

		Customer findCustomer = jpaCustomerRepository.findById(savedCustomer.getId()).orElseThrow();
		Assertions.assertEquals(newCustomer, findCustomer);
	}
}
