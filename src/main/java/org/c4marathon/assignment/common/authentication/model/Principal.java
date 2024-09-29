package org.c4marathon.assignment.common.authentication.model;

public interface Principal {
	Object getId();

	boolean isAnonymous();
}
