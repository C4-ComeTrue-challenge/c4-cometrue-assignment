package org.c4marathon.assignment.product.domain.repository;

import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
