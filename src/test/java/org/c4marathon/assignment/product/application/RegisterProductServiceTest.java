package org.c4marathon.assignment.product.application;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterProductServiceTest {
	@InjectMocks
	RegisterProductService registerProductService;
	@Mock
	ProductRepository productRepository;

	@Test
	@DisplayName("신규 상품 등록시 저장된 상품의 id를 반환한다.")
	void register() {
		UUID sellerId = UUID.randomUUID();
		Long nextId = 10L;

		RegisterProductService.Command cmd = new RegisterProductService.Command(sellerId, "newProduct",
			"this is new product", 1000L, 100L);
		when(productRepository.save(refEq(new Product(cmd.name(), cmd.description(), cmd.price(), cmd.stock()))))
			.thenReturn(
				new Product(nextId, cmd.name(), cmd.description(), cmd.price(), cmd.stock()));

		then(registerProductService.register(cmd)).isEqualTo(nextId);
		verify(productRepository)
			.save(refEq(new Product(cmd.name(), cmd.description(), cmd.price(), cmd.stock())));
	}
}
