package org.c4marathon.assignment.transaction.service;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.transaction.domain.Transaction;
import org.c4marathon.assignment.transaction.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final Long ADMINISTRATOR_ACCOUNT_ID = 0L;
    private static final String ADMINISTRATOR_ACCOUNT = "시스템 충전";

    @Transactional
    public void chargeCustomerCash(Long accountId, Long amount) {
        Account account = getAccountBy(accountId);
        Balance balance = account.getBalance();
        balance.withdraw(amount);

        Transaction transaction = Transaction.builder()
                .fromAccountId(ADMINISTRATOR_ACCOUNT_ID)
                .fromNickname(account.getNickname())
                .toAccountId(accountId)
                .toNickname(account.getNickname())
                .amount(amount)
                .balance(balance.getBalance())
                .build();

        transactionRepository.save(transaction);
    }

    private Account getAccountBy(Long fromAccountId) {
        return accountRepository.findAccountByIdWithPessimisticLock(fromAccountId)
                                .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

}
