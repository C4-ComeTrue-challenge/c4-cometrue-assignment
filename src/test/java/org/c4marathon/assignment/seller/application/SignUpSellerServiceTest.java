package org.c4marathon.assignment.seller.application;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.customer.domain.PasswordEncoder;
import org.c4marathon.assignment.seller.domain.Seller;
import org.c4marathon.assignment.seller.domain.SellerRepository;
import org.c4marathon.assignment.seller.domain.TestSellerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignUpSellerServiceTest {
	@InjectMocks
	SignUpSellerService signUpService;
	@Mock
	SellerRepository sellerRepository;
	@Mock
	PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("신규 회원 등록시 저장된 회원의 id를 반환한다.")
	void shouldReturnSavedUserId() {
		UUID nextId = UUID.randomUUID();

		SignUpSellerService.Command cmd = new SignUpSellerService.Command(
			"newEmail", "newPassword", "newName", "000-0000-000");
		when(passwordEncoder.encode(cmd.password())).thenReturn("{noop}" + cmd.password());
		when(sellerRepository.save(
			refEq(new Seller(cmd.email(), cmd.password(), cmd.name(), cmd.licenseNumber(), passwordEncoder), "point")))
			.thenReturn(
				TestSellerFactory.create(
					nextId, cmd.email(), "{noop}" + cmd.password(), cmd.name(), cmd.licenseNumber(), new Point()));

		then(signUpService.register(cmd)).isEqualTo(nextId);
		verify(sellerRepository)
			.save(refEq(new Seller(cmd.email(), cmd.password(), cmd.name(), cmd.licenseNumber(), passwordEncoder)));
	}
}
