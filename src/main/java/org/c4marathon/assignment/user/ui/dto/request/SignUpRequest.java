package org.c4marathon.assignment.user.ui.dto.request;

public record SignUpRequest(
	String email,
	String password,
	String name,
	String type
) {
}
