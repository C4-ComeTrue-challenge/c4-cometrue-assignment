package org.c4marathon.assignment.common.authentication.model.principal;

import java.util.UUID;

public class LoginSeller implements Principal {
	private final UUID id;

	public LoginSeller(UUID id) {
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
