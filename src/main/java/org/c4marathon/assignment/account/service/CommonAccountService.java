package org.c4marathon.assignment.account.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.transaction.domain.repository.TransactionQueryRepository;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.account.dto.response.AccountResponse;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;
import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Service
@RequiredArgsConstructor
public class CommonAccountService {

    private final AccountRepository accountRepository;
    private final TransactionQueryRepository accountQueryRepository;

    @Transactional
    public AccountResponse showAccountInfo(MemberAuthority authority, Long memberAuthId, Long transactionCursorId) {

        Account account = accountRepository.findAccountByAuthorityAndMemberAuthId(authority, memberAuthId)
                                           .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
        List<TransactionDto> transactions = accountQueryRepository.getTransactions(account.getId(), transactionCursorId);
        Boolean hasNext = transactions.size() > SMALL_PAGE_SIZE;
        Integer size = hasNext ? SMALL_PAGE_SIZE : transactions.size();
        Long transactionCursor = hasNext ? transactions.get(SMALL_PAGE_SIZE - 1).transactionId() : null;

        return new AccountResponse(hasNext, size, transactionCursor,
                                   account.getNickname(), account.getBalance().getBalance(), transactions);
    }

    @Transactional
    public void createMerchantAccount(String nickname, Balance money, Long merchantId) {
        accountRepository.save(Account.of(nickname, money, MERCHANT, merchantId));
    }

    @Transactional
    public void createCustomerAccount(String nickname, Balance money, Long customerId) {
        accountRepository.save(Account.of(nickname, money, CUSTOMER, customerId));
    }
}
