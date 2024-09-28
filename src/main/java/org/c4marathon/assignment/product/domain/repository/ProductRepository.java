package org.c4marathon.assignment.product.domain.repository;


import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
