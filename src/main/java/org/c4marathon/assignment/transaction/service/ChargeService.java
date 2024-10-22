package org.c4marathon.assignment.transaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final TransactionService transactionCustomerService;
    private static final Long ADMINISTRATOR_ACCOUNT_ID = 1L;

    @Transactional
    public void chargeCash(Long toAccountId, Long money) {
        transactionCustomerService.doTransaction(ADMINISTRATOR_ACCOUNT_ID, toAccountId, money);
    }

}
