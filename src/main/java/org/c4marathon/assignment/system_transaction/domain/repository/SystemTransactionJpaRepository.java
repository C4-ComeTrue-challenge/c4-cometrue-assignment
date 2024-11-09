package org.c4marathon.assignment.system_transaction.domain.repository;

import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.c4marathon.assignment.system_transaction.domain.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemTransactionJpaRepository extends JpaRepository<SystemTransaction, Long> {

    Optional<SystemTransaction> findByStatusAndFromAccountId(TransactionStatus status, Long fromAccountId);
}
