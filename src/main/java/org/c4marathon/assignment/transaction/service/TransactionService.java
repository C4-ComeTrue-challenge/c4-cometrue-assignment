package org.c4marathon.assignment.transaction.service;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

import java.math.BigDecimal;

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
public class TransactionService {

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.05");
    private static final Long OPERATOR_ACCOUNT_ID = 1L;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void doTransaction(Long fromAccountId, Long toAccountId, Long money) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(()
                                                            -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(()
                                                            -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));

        Balance fromBalance = fromAccount.getBalance();
        fromBalance.withdraw(money);
        Balance toBalance = toAccount.getBalance();
        toBalance.deposit(money);

        Transaction fromTransaction = Transaction.of(fromAccount, fromAccountId, fromAccount.getNickname(),
                                                      toAccountId, toAccount.getNickname(),
                                                      money, fromBalance);
        Transaction toTransaction = Transaction.of(toAccount, fromAccountId, fromAccount.getNickname(),
                                                   toAccountId, toAccount.getNickname(),
                                                   money, toBalance);

        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
    }

    @Transactional
    public void payForProduct(Long customerId, Long merchantId, Long money) {
        Account fromAccount = accountRepository.findAccountByAuthorityAndMemberAuthId(CUSTOMER, customerId)
                .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
        Account toAccount = accountRepository.findAccountByAuthorityAndMemberAuthId(MERCHANT, merchantId)
                .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));
        Account operatorAccount = accountRepository.findById(OPERATOR_ACCOUNT_ID)
                .orElseThrow(() -> new AccountException(ExceptionCode.ACCOUNT_NOT_FOUND));

        Long commissionPrice = commissionPrice(money);
        Balance fromBalance = fromAccount.getBalance();
        fromBalance.withdraw(money);
        Balance toBalance = toAccount.getBalance();
        toBalance.deposit(money - commissionPrice);
        Balance operatorBalance = operatorAccount.getBalance();
        operatorBalance.deposit(commissionPrice);

        Transaction fromTransaction = Transaction.of(fromAccount, customerId, fromAccount.getNickname(),
                merchantId, toAccount.getNickname(),
                money, fromBalance);
        Transaction toTransaction = Transaction.of(toAccount, customerId, fromAccount.getNickname(),
                merchantId, toAccount.getNickname(),
                money - commissionPrice, toBalance);
        Transaction operatorTransaction = Transaction.of(operatorAccount, merchantId, toAccount.getNickname(),
                OPERATOR_ACCOUNT_ID, operatorAccount.getNickname(),
                commissionPrice, operatorBalance);

        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
        transactionRepository.save(operatorTransaction);
    }

    private Long commissionPrice(Long money) {
        BigDecimal principal = new BigDecimal(money);
        return principal.multiply(COMMISSION_RATE).longValue();
    }

}
