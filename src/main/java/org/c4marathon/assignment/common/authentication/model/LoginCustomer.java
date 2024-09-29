package org.c4marathon.assignment.common.authentication.model;

import java.util.UUID;

public class LoginCustomer implements Principal {
	private final UUID id;

	public LoginCustomer(UUID id) {
		this.id = id;
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}
}
