package org.c4marathon.assignment.common.authentication.model.principal;

public interface Principal {
	Object getId();

	boolean isAnonymous();
}
