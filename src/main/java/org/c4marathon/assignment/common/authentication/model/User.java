package org.c4marathon.assignment.common.authentication.model;

import java.util.Collection;
import java.util.List;

public interface User {
	String getEmail();

	String getPassword();

	String getRole();

	default String getUsername() {
		return getEmail();
	}

	default Collection<?> getAuthorities() {
		return List.of("ROLE_" + getRole());
	}
}
