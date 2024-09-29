package org.c4marathon.assignment.transaction.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.transaction.domain.Transaction;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.transaction.domain.repository.TransactionRepository;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TransactionCustomerService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void doTransaction(Long fromAccountId, Long toAccountId, Long money) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                                               .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
        Account toAccount = accountRepository.findById(toAccountId)
                                               .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));

        Balance fromBalance = fromAccount.getBalance().withdraw(money);
        Balance toBalance = toAccount.getBalance().deposit(money);

        Transaction fromTransaction = Transaction.of(fromAccount, fromAccountId, fromAccount.getNickname(),
                                                      toAccountId, toAccount.getNickname(),
                                                      money, fromBalance);
        Transaction toTransaction = Transaction.of(toAccount, fromAccountId, fromAccount.getNickname(),
                                                   toAccountId, toAccount.getNickname(),
                                                   money, toBalance);

        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
    }


}
