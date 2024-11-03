package org.c4marathon.assignment.account.service;

import static java.util.Comparator.comparing;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;
import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.account.dto.response.AccountResponse;
import org.c4marathon.assignment.global.exception.AccountException;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.transaction.domain.repository.TransactionQueryRepository;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonAccountService {

    private final AccountRepository accountRepository;
    private final TransactionQueryRepository transactionQueryRepository;

    @Transactional
    public AccountResponse showAccountInfo(MemberAuthority authority, Long memberAuthId, LocalDateTime cursorDate) {

        Account account = getAccountBy(authority, memberAuthId);
        List<TransactionDto> transactions = transactionQueryRepository.getTransactionsByAccountIdAndTransactionDate(account.getId(), cursorDate);

        Boolean hasNext = transactions.size() > SMALL_PAGE_SIZE;
        Integer size = hasNext ? SMALL_PAGE_SIZE : transactions.size();
        LocalDateTime transactionDateCursor = hasNext ? transactions.get(SMALL_PAGE_SIZE - 1).transactionDate() : null;
        if (hasNext) {
            transactions.remove(SMALL_PAGE_SIZE);
        }
        return new AccountResponse(hasNext, size, transactionDateCursor,
                                   account.getNickname(), account.getBalance().getBalance(), transactions);
    }

    @Transactional
    public Account findAccountByAuthorityAndMemberAuthId(MemberAuthority authority, Long memberAuthId) {
        return getAccountBy(authority, memberAuthId);
    }

    @Transactional
    public void createMerchantAccount(String nickname, Balance money, Long merchantId) {
        accountRepository.save(Account.of(nickname, money, MERCHANT, merchantId));
    }

    @Transactional
    public void createCustomerAccount(String nickname, Balance money, Long customerId) {
        accountRepository.save(Account.of(nickname, money, CUSTOMER, customerId));
    }

    private Account getAccountBy(MemberAuthority authority, Long memberAuthId) {
        return accountRepository.findAccountByAuthorityAndMemberAuthId(authority, memberAuthId)
                                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));
    }
}
