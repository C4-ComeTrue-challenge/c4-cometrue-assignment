package org.c4marathon.assignment.system_transaction.domain.repository;

import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemTransactionJpaRepository extends JpaRepository<SystemTransaction, Long> {
}
