package org.c4marathon.assignment.common.authentication.model;

import java.util.Collection;

import org.c4marathon.assignment.common.authentication.model.principal.Principal;

public interface Authentication {
	Principal getPrincipal();

	Collection<?> getAuthorities();

	boolean isAuthenticated();
}
