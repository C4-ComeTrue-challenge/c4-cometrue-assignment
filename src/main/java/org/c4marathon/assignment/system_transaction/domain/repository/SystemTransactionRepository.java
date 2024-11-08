package org.c4marathon.assignment.system_transaction.domain.repository;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SystemTransactionRepository {

    private final SystemTransactionJpaRepository systemTransactionJpaRepository;

    public void save(SystemTransaction systemTransaction) {
        systemTransactionJpaRepository.save(systemTransaction);
    }
}
