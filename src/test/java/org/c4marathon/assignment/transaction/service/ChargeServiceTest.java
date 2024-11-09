package org.c4marathon.assignment.transaction.service;

import org.assertj.core.api.Assertions;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.c4marathon.assignment.member.service.MemberAccountFacadeService;
import org.c4marathon.assignment.system_transaction.domain.SystemTransaction;
import org.c4marathon.assignment.system_transaction.domain.TransactionStatus;
import org.c4marathon.assignment.system_transaction.domain.repository.SystemTransactionJpaRepository;
import org.c4marathon.assignment.transaction.domain.Transaction;
import org.c4marathon.assignment.transaction.domain.repository.TransactionQueryRepository;
import org.c4marathon.assignment.transaction.domain.repository.TransactionRepository;
import org.c4marathon.assignment.transaction.dto.TransactionDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.c4marathon.assignment.system_transaction.domain.TransactionStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChargeServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SystemTransactionJpaRepository systemTransactionJpaRepository;

    @Autowired
    private MemberAccountFacadeService memberAccountFacadeService;

    @Autowired
    private ChargeService chargeService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        merchantRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
        transactionRepository.deleteAllInBatch();
        systemTransactionJpaRepository.deleteAllInBatch();
    }

    @DisplayName("고객은 시스템을 통해서 충전을 할 수 있고, 이 때, 시스템 이체 내역과 트랜잭션이 남는다.")
    @Test
    void 구매자_금액_충전_테스트() {
        String username = "customer";
        String password = "password";
        memberAccountFacadeService.createCustomerMemberAndAccount(username, password);

        Member findMember = memberRepository.findByNickname(username).get();
        MemberAuthority authority = findMember.getAuthority();
        Long customerId = findMember.getId();
        Account findAccount = accountRepository.findAccountByAuthorityAndMemberAuthId(authority, customerId).get();

        chargeService.chargeCustomerCash(findAccount.getId(), 10000L);

        Account afterAccount = accountRepository.findAccountByAuthorityAndMemberAuthId(authority, customerId).get();
        assertThat(afterAccount.getTotalBalance()).isEqualTo(10000L);

        Transaction findTransaction = transactionRepository.findByFromAccountId(0L).get();
        assertThat(findTransaction).extracting("toAccountId", "toNickname", "amount", "balance", "memo")
                .contains(afterAccount.getId(), "customer", 10000L, 10000L, "포인트 충전 : 10000원");

        var systemTransaction
                = systemTransactionJpaRepository.findByStatusAndFromAccountId(COMPLETED, 0L).get();
        assertThat(systemTransaction.getStatus()).isEqualTo(COMPLETED);
        assertThat(systemTransaction.getToAccountId()).isEqualTo(findAccount.getId());
        assertThat(systemTransaction.getAmount()).isEqualTo(10000L);
    }

}