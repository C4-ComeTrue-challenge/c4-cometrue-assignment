package org.c4marathon.assignment.member.domain.repository;

import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Customer> findByMemberId(Long memberId);
}
