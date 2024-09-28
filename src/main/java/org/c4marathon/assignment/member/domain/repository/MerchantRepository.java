package org.c4marathon.assignment.member.domain.repository;

import org.c4marathon.assignment.member.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
