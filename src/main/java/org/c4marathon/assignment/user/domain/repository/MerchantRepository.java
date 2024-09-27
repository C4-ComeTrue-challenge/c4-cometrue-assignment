package org.c4marathon.assignment.user.domain.repository;

import org.c4marathon.assignment.user.domain.Customer;
import org.c4marathon.assignment.user.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
