package org.c4marathon.assignment.account.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.domain.repository.AccountQueryRepository;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.account.dto.response.AccountDto;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountQueryRepository accountQueryRepository;

    @Transactional
    public void createAccount(String nickname, Balance money, MemberAuthority authority, Long memberAuthId) {
        if (authority.equals(MERCHANT)) {
            createMerchantAccount(nickname, money, memberAuthId);
        } else {
            createCustomerAccount(nickname, money, memberAuthId);
        }
    }

    @Transactional
    public AccountDto showAccountInfo(MemberAuthority authority, Long memberAuthId, Long transactionCursorId) {

        Account account = accountRepository.findAccountByAuthorityAndMemberAuthId(authority, memberAuthId)
                                           .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        List<TransactionDto> transactions = accountQueryRepository.getTransactions(account.getId(), transactionCursorId);

        return new AccountDto(account.getNickname(), account.getBalance().getBalance(), transactions);
    }

    private void createMerchantAccount(String nickname, Balance money, Long merchantId) {
        accountRepository.save(Account.of(nickname, money, MERCHANT, merchantId));
    }

    private void createCustomerAccount(String nickname, Balance money, Long customerId) {
        accountRepository.save(Account.of(nickname, money, CUSTOMER, customerId));
    }
}
