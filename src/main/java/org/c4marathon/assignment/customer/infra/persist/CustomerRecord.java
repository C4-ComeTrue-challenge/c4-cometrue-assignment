package org.c4marathon.assignment.customer.infra.persist;

import java.util.UUID;

import org.c4marathon.assignment.common.entity.Point;
import org.c4marathon.assignment.customer.domain.Customer;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "CUSTOMERS",
	uniqueConstraints = {@UniqueConstraint(name = "uq_customers_email", columnNames = {"email"})}
)
public class CustomerRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	private UUID id;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	@Embedded
	private Point point;

	protected CustomerRecord() {
	}

	public CustomerRecord(UUID id, String email, String password, String name, Point point) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.point = point;
	}

	public Customer toDomain() {
		return new Customer(id, email, password, name, point);
	}
}
