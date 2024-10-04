package org.c4marathon.assignment.seller.infra.persist;

import java.lang.reflect.Field;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.seller.domain.Seller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaSellerRepository.class})
class JpaSellerRepositoryTest {
	@Autowired
	private JpaSellerRepository jpaSellerRepository;

	@Test
	void save() throws IllegalAccessException {
		Seller newSeller = new Seller(null, "newEmail", "newPassword", "newName", "newLicenseNumber", new Point());
		Seller savedSeller = jpaSellerRepository.save(newSeller);

		Seller findSeller = jpaSellerRepository.findById(savedSeller.getId()).orElseThrow();
		assertRefEquals(savedSeller, findSeller);
	}

	private void assertRefEquals(Seller expected, Seller actual) throws IllegalAccessException {
		Field[] fields = Seller.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);

			Object value1 = field.get(expected);
			Object value2 = field.get(actual);

			Assertions.assertEquals(value1, value2);
		}
	}
}
