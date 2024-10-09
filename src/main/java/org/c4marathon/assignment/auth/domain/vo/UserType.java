package org.c4marathon.assignment.auth.domain.vo;

import java.util.UUID;
import java.util.function.Function;

import org.c4marathon.assignment.common.authentication.model.principal.LoginCustomer;
import org.c4marathon.assignment.common.authentication.model.principal.LoginSeller;
import org.c4marathon.assignment.common.authentication.model.principal.Principal;

public enum UserType {
	CUSTOMER(LoginCustomer::new), SELLER(LoginSeller::new);

	private final Function<UUID, Principal> principalConstructor;

	UserType(Function<UUID, Principal> principalConstructor) {
		this.principalConstructor = principalConstructor;
	}

	public Principal getPrincipal(UUID uuid) {
		return principalConstructor.apply(uuid);
	}
}
