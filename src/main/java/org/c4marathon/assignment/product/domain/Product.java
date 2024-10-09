package org.c4marathon.assignment.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTS")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String description;
	@Column(nullable = false)
	private Long price;
	@Column(nullable = false)
	private Long stock;

	protected Product() {
	}

	public Product(String name, String description, Long price, Long stock) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
	}

	public Product(Long id, String name, String description, Long price, Long stock) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
	}

	public Long getId() {
		return id;
	}
}
