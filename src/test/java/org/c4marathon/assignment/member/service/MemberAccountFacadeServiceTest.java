package org.c4marathon.assignment.member.service;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.repository.AccountRepository;
import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountFacadeServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MemberAccountFacadeService memberAccountFacadeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        merchantRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @DisplayName("판매자 고객은 회원 등록과 동시에 계좌를 한 번에 만들 수 있다.")
    @Test
    void 판매자_고객_회원등록_계좌등록() {
        String nickname = "test";
        String password = "password!";

        memberAccountFacadeService.createMerchantMemberAndAccount(nickname, password);

        Member findMember = memberRepository.findByNickname("test").get();
        assertThat(findMember).extracting("merchantId", "nickname", "authority")
                                .containsExactly(1L, "test", MERCHANT);
        assertTrue(passwordEncoder.matches(password, findMember.getPassword()));

        Merchant findMerchant = merchantRepository.findByMemberId(findMember.getId()).get();
        assertThat(findMerchant.getMemberId()).isEqualTo(findMember.getId());

        Account findAccount
                = accountRepository.findAccountByAuthorityAndMemberAuthId(MERCHANT, findMerchant.getId()).get();
        assertThat(findAccount).extracting("nickname", "authority", "memberAuthId")
                                .containsExactly("test", MERCHANT, findMerchant.getId());
        assertThat(findAccount.getBalance().getBalance()).isEqualTo(0L);
    }

    @DisplayName("구매자 고객은 회원 등록과 동시에 계좌를 한 번에 만들 수 있다.")
    @Test
    void 구매자_고객_회원등록_계좌등록() {
        String nickname = "test";
        String password = "password!";

        memberAccountFacadeService.createCustomerMemberAndAccount(nickname, password);

        Member findMember = memberRepository.findByNickname("test").get();
        assertThat(findMember).extracting("customerId", "nickname", "authority")
                .containsExactly(1L, "test", CUSTOMER);
        assertTrue(passwordEncoder.matches(password, findMember.getPassword()));

        Customer findCustomer = customerRepository.findByMemberId(findMember.getId()).get();
        assertThat(findCustomer.getMemberId()).isEqualTo(findMember.getId());

        Account findAccount
                = accountRepository.findAccountByAuthorityAndMemberAuthId(CUSTOMER, findCustomer.getId()).get();
        assertThat(findAccount).extracting("nickname", "authority", "memberAuthId")
                .containsExactly("test", CUSTOMER, findCustomer.getId());
        assertThat(findAccount.getBalance().getBalance()).isEqualTo(0L);
    }
}