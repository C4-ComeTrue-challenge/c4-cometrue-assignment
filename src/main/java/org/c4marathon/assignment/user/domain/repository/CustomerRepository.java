package org.c4marathon.assignment.user.domain.repository;

import org.c4marathon.assignment.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
