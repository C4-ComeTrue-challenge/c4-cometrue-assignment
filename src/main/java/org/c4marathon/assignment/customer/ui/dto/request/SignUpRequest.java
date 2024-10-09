package org.c4marathon.assignment.customer.ui.dto.request;

public record SignUpRequest(
	String email,
	String password,
	String name
) {
}
