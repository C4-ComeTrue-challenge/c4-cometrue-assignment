package org.c4marathon.assignment.auth.ui.dto.request;

public record SignInRequest(
	String email,
	String password
) {
}
