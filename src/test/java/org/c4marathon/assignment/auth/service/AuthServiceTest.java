package org.c4marathon.assignment.auth.service;

import io.jsonwebtoken.Jwts;
import org.c4marathon.assignment.auth.domain.Session;
import org.c4marathon.assignment.auth.domain.repository.SessionRepository;
import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.auth.util.AuthTokenContext.*;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthTokenContext authTokenContext;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

//    @DisplayName("로그인되어있지 않은 계정에 대해 로그인 시 Refresh 토큰이 발급된다.")
//    @Test
//    void loginAndStoreRefreshToken() {
//        // given
//        Member savedMember = memberRepository.save(Member.customer("tester", "test"));
//        Customer customer = customerRepository.save(Customer.of(savedMember.getId(), savedMember.getNickname()));
//        savedMember.addCustomer(customer);
//        String refreshToken = generateRefreshToken(savedMember.getId().toString());
//        // when
//        authService.loginAndStoreRefreshToken(CUSTOMER, refreshToken);
//        Session findSession = sessionRepository.findByAuthorityAndMemberAuthId(CUSTOMER, savedMember.getCustomerId());
//        // then
//        assertThat(findSession)
//                .extracting("memberAuthId", "authority", "isBlackList")
//                .containsExactly(
//                        1L, CUSTOMER, false
//                );
//    }

    private String generateRefreshToken(String memberId) {
        return Jwts.builder()
                .claim(MEMBER_ID, memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(authTokenContext.getSecretKey()).compact();
    }
}