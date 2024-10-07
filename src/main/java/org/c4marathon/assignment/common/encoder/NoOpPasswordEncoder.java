package org.c4marathon.assignment.common.encoder;

import java.util.Objects;

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

	@Override
	public boolean matches(String password, String encodedPassword) {
		return Objects.equals(encode(password), encodedPassword);
	}
}
