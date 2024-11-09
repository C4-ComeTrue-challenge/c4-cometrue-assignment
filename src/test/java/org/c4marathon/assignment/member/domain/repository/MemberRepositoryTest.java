package org.c4marathon.assignment.member.domain.repository;

import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
        merchantRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("구매자 고객은 Member의 식별자로 찾을 수 있다.")
    @Test
    void 구매자_고객을_등록한다() {
        String nickname = "test";
        String password = "password!";

        memberService.registerCustomerUser(nickname, password);

        Member findMember = memberRepository.findByNickname("test").get();
        assertThat(findMember).extracting("customerId", "nickname", "authority")
                        .containsExactly(1L, "test", CUSTOMER);
        assertTrue(passwordEncoder.matches(password, findMember.getPassword()));

        Customer findCustomer = customerRepository.findByMemberId(findMember.getId()).get();
        assertThat(findCustomer.getMemberId()).isEqualTo(findMember.getId());
    }

    @DisplayName("판매자 고객은 Member의 식별자로 찾을 수 있다.")
    @Test
    void 판매자_고객은_Member의_식별자로_찾을_수_있다() {
        String nickname = "test";
        String password = "password!";

        memberService.registerMerchantUser(nickname, password);
        Member findMember = memberRepository.findByNickname("test").get();

        assertThat(findMember).extracting("merchantId", "nickname", "authority")
                .containsExactly(1L, "test", MERCHANT);
        assertTrue(passwordEncoder.matches(password, findMember.getPassword()));

        Merchant findMerchant = merchantRepository.findByMemberId(findMember.getId()).get();
        assertThat(findMerchant.getMemberId()).isEqualTo(findMember.getId());
    }
}