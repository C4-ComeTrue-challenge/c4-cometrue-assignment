package org.c4marathon.assignment.member.domain.repository;

import org.c4marathon.assignment.member.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
