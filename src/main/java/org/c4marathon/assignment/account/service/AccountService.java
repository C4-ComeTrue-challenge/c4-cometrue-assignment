package org.c4marathon.assignment.account.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.account.dto.AccountDto;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void createAccount(Balance money, MemberAuthority authority, Long memberAuthId) {
        if (authority.equals(MERCHANT)) {
            createMerchantAccount(money, memberAuthId);
        } else {
            createCustomerAccount(money, memberAuthId);
        }
    }

//    @Transactional
//    public AccountDto showAccountInfo(MemberAuthority authority, Long memberAuthId) {
//
//    }

    @Transactional
    protected void createMerchantAccount(Balance money, Long merchantId) {
        accountRepository.save(Account.of(money, MERCHANT, merchantId));
    }

    @Transactional
    protected void createCustomerAccount(Balance money, Long customerId) {
        accountRepository.save(Account.of(money, CUSTOMER, customerId));
    }
}
