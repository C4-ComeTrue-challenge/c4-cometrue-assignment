package org.c4marathon.assignment.seller.domain;

import java.util.UUID;

import org.c4marathon.assignment.common.encoder.PasswordEncoder;
import org.c4marathon.assignment.common.entity.Point;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(
	name = "SELLERS"
)
public class Seller {
	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	private UUID id;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String licenseNumber;
	@Embedded
	private Point point;

	protected Seller() {
	}

	Seller(UUID id, String email, String encodedPassword, String name, String licenseNumber, Point point) {
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
}
