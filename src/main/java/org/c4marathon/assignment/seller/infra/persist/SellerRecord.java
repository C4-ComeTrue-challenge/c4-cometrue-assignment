package org.c4marathon.assignment.seller.infra.persist;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.seller.domain.Seller;
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
public class SellerRecord {
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

	protected SellerRecord() {
	}

	public SellerRecord(UUID id, String email, String password, String name, String licenseNumber, Point point) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.licenseNumber = licenseNumber;
		this.point = point;
	}

	public Seller toDomain() {
		return new Seller(id, email, password, name, licenseNumber, point);
	}
}
