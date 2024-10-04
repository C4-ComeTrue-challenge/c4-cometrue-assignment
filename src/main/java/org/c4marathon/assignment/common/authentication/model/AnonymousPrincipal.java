package org.c4marathon.assignment.common.authentication.model;

public class AnonymousPrincipal implements Principal {
	@Override
	public Object getId() {
		return null;
	}

	@Override
	public boolean isAnonymous() {
		return true;
	}
}