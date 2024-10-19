package org.c4marathon.assignment.customer.infra.persist;

import java.lang.reflect.Field;

import org.c4marathon.assignment.common.entity.Point;
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
	void save() throws IllegalAccessException {
		Customer newCustomer = new Customer(null, "newEmail", "newPassword", "newName", new Point());
		Customer savedCustomer = jpaCustomerRepository.save(newCustomer);

		Customer findCustomer = jpaCustomerRepository.findById(savedCustomer.getId()).orElseThrow();
		assertRefEquals(savedCustomer, findCustomer);
	}

	private void assertRefEquals(Customer expected, Customer actual) throws IllegalAccessException {
		Field[] fields = Customer.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);

			Object value1 = field.get(expected);
			Object value2 = field.get(actual);

			Assertions.assertEquals(value1, value2);
		}
	}
}
