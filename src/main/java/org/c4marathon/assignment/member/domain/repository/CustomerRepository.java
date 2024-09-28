package org.c4marathon.assignment.member.domain.repository;

import org.c4marathon.assignment.member.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByMemberId(Long memberId);
}
