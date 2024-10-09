package org.c4marathon.assignment.common.authentication.model;

import java.io.Serializable;
import java.util.Collection;

import org.c4marathon.assignment.common.authentication.model.principal.Principal;

public interface Authentication extends Serializable {
	Principal getPrincipal();

	Collection<?> getAuthorities();

	boolean isAuthenticated();
}
