package org.c4marathon.assignment.transaction.service;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode;
import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.c4marathon.assignment.system_transaction.domain.repository.SystemTransactionRepository;
import org.c4marathon.assignment.system_transaction.service.SystemTransactionService;
import org.c4marathon.assignment.transaction.domain.Transaction;
import org.c4marathon.assignment.transaction.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.INVALID_REQUEST;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final Long ADMINISTRATOR_ACCOUNT_ID = 0L;
    private static final String ADMINISTRATOR_ACCOUNT = "시스템";
    private final SystemTransactionService systemTransactionService;

    @Transactional
    public void chargeCustomerCash(Long accountId, Long amount) {
        Account account = getAccountBy(accountId);
        customerValidation(account);

        Balance balance = account.getBalance();
        balance.deposit(amount);

        Transaction transaction = Transaction.builder()
                .fromAccountId(ADMINISTRATOR_ACCOUNT_ID)
                .fromNickname(ADMINISTRATOR_ACCOUNT)
                .toAccountId(accountId)
                .toNickname(account.getNickname())
                .amount(amount)
                .balance(balance.getBalance())
                .memo("포인트 충전 : " + amount + "원")
                .build();

        transactionRepository.save(transaction);

        systemTransactionService.saveChargeTransaction(accountId, amount);
    }

    private void customerValidation(Account account) {
        if (!account.getAuthority().equals(CUSTOMER)) {
            throw new AccountException(INVALID_REQUEST);
        }
    }

    private Account getAccountBy(Long fromAccountId) {
        return accountRepository.findAccountByIdWithPessimisticLock(fromAccountId)
                                .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

}
