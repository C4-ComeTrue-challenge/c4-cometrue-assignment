package org.c4marathon.assignment.common.encoder;

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
