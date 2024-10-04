package org.c4marathon.assignment.customer.application;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.c4marathon.assignment.common.encoder.PasswordEncoder;
import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.customer.domain.Customer;
import org.c4marathon.assignment.customer.domain.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {
	@InjectMocks
	SignUpService signUpService;
	@Mock
	CustomerRepository customerRepository;
	@Mock
	PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("신규 회원 등록시 저장된 회원의 id를 반환한다.")
	void shouldReturnSavedUserId() {
		UUID nextId = UUID.randomUUID();

		SignUpService.Command cmd = new SignUpService.Command("newEmail", "newPassword", "newName");
		when(passwordEncoder.encode(cmd.password())).thenReturn("{noop}" + cmd.password());
		when(customerRepository.save(
			refEq(new Customer(cmd.email(), cmd.password(), cmd.name(), passwordEncoder), "point")))
			.thenReturn(
				new Customer(nextId, cmd.email(), "{noop}" + cmd.password(), cmd.name(), new Point()));

		then(signUpService.register(cmd)).isEqualTo(nextId);
		verify(customerRepository)
			.save(refEq(new Customer(cmd.email(), cmd.password(), cmd.name(), passwordEncoder), "point"));
	}
}
