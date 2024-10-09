package org.c4marathon.assignment.common.encoder;

public interface PasswordEncoder {
	String encode(String password);

	boolean matches(String password, String encodedPassword);
}
