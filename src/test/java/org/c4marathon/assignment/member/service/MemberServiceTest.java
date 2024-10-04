package org.c4marathon.assignment.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

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

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
        merchantRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("구매자가 등록된다.")
    @Test
    void customerRegisterTest() {
        // given // when
        memberService.registerCustomerUser("구매자", "123456");

        // then
        Member findMember = memberRepository.findByNickname("구매자").get();
        Customer findCustomer = customerRepository.findByMemberId(findMember.getId()).get();

        assertThat(findMember)
                .extracting("nickname", "authority")
                .containsExactly("구매자", CUSTOMER);
        assertThat(findCustomer)
                .extracting("memberId", "nickname")
                .containsExactly(findMember.getId(), "구매자");
    }

    @DisplayName("판매자가 등록된다.")
    @Test
    void merchantRegisterTest() {
        // given // when
        memberService.registerMerchantUser("판매자", "123456");

        // then
        Member findMember = memberRepository.findByNickname("판매자").get();
        Merchant findMerchant = merchantRepository.findByMemberId(findMember.getId()).get();

        assertThat(findMember)
                .extracting("nickname", "authority")
                .containsExactly("판매자", MERCHANT);

        assertThat(findMerchant)
                .extracting("memberId", "nickname")
                .containsExactly(findMember.getId(), "판매자");
    }
}
