package org.c4marathon.assignment.seller.domain;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.customer.domain.PasswordEncoder;

public class Seller {

	Seller(UUID id, String email, String encodedPassword, String name, String licenseNumber, Point point) {

	}

	public Seller(String email, String password, String name, String licenseNumber, PasswordEncoder passwordEncoder) {

	}

	public UUID getId() {
		return null;
	}
}
