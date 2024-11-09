package org.c4marathon.assignment.system_transaction.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.c4marathon.assignment.system_transaction.domain.repository.SystemTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemTransactionService {

    private final SystemTransactionRepository systemTransactionRepository;

    @Transactional
    public void saveChargeTransaction(Long customerAccountId, Long amount) {
        var systemTransaction = SystemTransaction.charge(customerAccountId, amount);
        systemTransactionRepository.save(systemTransaction);
    }

    @Transactional
    public void saveBillingTransaction(Long customerAccountId, Long merchantAccountId, Long amount) {
        var systemTransaction = SystemTransaction.billing(customerAccountId, merchantAccountId, amount);
        systemTransactionRepository.save(systemTransaction);
    }
}
