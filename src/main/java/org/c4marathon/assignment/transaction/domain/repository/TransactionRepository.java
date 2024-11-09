package org.c4marathon.assignment.transaction.domain.repository;

import org.c4marathon.assignment.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByFromAccountId(Long fromAccountId);
}
