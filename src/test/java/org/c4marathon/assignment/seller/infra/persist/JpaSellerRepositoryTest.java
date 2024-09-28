package org.c4marathon.assignment.seller.infra.persist;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.seller.domain.Seller;
import org.c4marathon.assignment.seller.domain.TestSellerFactory;
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
	void save() {
		Seller newSeller = TestSellerFactory.create(
			null, "newEmail", "newPassword", "newName", "newLicenseNumber", new Point());
		Seller savedSeller = jpaSellerRepository.save(newSeller);

		Seller findSeller = jpaSellerRepository.findById(savedSeller.getId()).orElseThrow();
		Assertions.assertEquals(newSeller, findSeller);
	}
}
