package org.c4marathon.assignment.seller.ui.dto.request;

public record SignUpRequest(
	String email,
	String password,
	String name,
	String licenseNumber
) {
}
