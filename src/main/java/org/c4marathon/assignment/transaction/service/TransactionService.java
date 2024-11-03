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


    private Long commissionPrice(Long money) {
        BigDecimal principal = new BigDecimal(money);
        return principal.multiply(COMMISSION_RATE).longValue();
    }

}
