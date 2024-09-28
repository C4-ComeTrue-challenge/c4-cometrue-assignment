package org.c4marathon.assignment.customer.infra.passwordencoder;

import org.c4marathon.assignment.customer.domain.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class NoOpPasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(String password) {
		if (password == null || password.isEmpty()) {
			return null;
		}
		return "{noop}" + password;
	}
}
