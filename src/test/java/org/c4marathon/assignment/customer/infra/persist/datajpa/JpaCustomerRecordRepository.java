package org.c4marathon.assignment.customer.infra.persist.datajpa;

import java.util.UUID;

import org.c4marathon.assignment.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCustomerRecordRepository extends JpaRepository<Customer, UUID> {
}
