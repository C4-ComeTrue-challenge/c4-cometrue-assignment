package org.c4marathon.assignment.order.application;

import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.UUID;

import org.c4marathon.assignment.order.domain.Cart;
import org.c4marathon.assignment.order.domain.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddToCartServiceTest {
	@InjectMocks
	AddToCartService addToCartService;
	@Mock
	CartRepository cartRepository;

	@Test
	@DisplayName("카트에 상품을 담을 수 있다.")
	void addItem() {
		UUID customerId = UUID.randomUUID();
		Long productId = 1L;
		int quantity = 2;
		int version = 3;

		AddToCartService.Command cmd = new AddToCartService.Command(customerId, productId, quantity, version);
		when(cartRepository.findByCustomerId(customerId))
			.thenReturn(Optional.of(new Cart(customerId)));

		addToCartService.add(cmd);

		verify(cartRepository, times(1)).save(any(Cart.class));
	}

	@Test
	@DisplayName("카트가 생성되어 있지 않으면 새로 생성하고 상품을 담는다.")
	void createAndAddItem() {
		UUID customerId = UUID.randomUUID();
		Long productId = 1L;
		int quantity = 2;
		int version = 3;

		AddToCartService.Command cmd = new AddToCartService.Command(customerId, productId, quantity, version);
		when(cartRepository.findByCustomerId(customerId))
			.thenReturn(Optional.empty());

		addToCartService.add(cmd);

		verify(cartRepository, times(1)).save(any(Cart.class));
	}
}
