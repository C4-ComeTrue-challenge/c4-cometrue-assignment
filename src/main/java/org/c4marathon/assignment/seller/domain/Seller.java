package org.c4marathon.assignment.seller.domain;

import java.util.UUID;

import org.c4marathon.assignment.common.encoder.PasswordEncoder;
import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.seller.infra.persist.SellerRecord;

public class Seller {
	private UUID id;
	private String email;
	private String password;
	private String name;
	private String licenseNumber;
	private Point point;

	public Seller(UUID id, String email, String encodedPassword, String name, String licenseNumber, Point point) {
		this.id = id;
		this.email = email;
		this.password = encodedPassword;
		this.name = name;
		this.licenseNumber = licenseNumber;
		this.point = point;
	}

	public Seller(String email, String password, String name, String licenseNumber, PasswordEncoder passwordEncoder) {
		this.email = email;
		this.password = passwordEncoder.encode(password);
		this.name = name;
		this.licenseNumber = licenseNumber;
		this.point = new Point();
	}

	public UUID getId() {
		return id;
	}

	public SellerRecord toRecord() {
		return new SellerRecord(id, email, password, name, licenseNumber, point);
	}
}
