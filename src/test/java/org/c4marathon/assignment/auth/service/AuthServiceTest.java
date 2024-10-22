package org.c4marathon.assignment.auth.service;

import static org.c4marathon.assignment.auth.util.AuthTokenContext.MEMBER_ID;
import static org.c4marathon.assignment.auth.util.AuthTokenContext.REFRESH_TOKEN_EXPIRATION_TIME;

import java.util.Date;

import org.c4marathon.assignment.auth.domain.repository.SessionRepository;
import org.c4marathon.assignment.auth.util.AuthTokenContext;
import org.c4marathon.assignment.auth.util.TokenHandler;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;

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


    private String generateRefreshToken(String memberId) {
        return Jwts.builder()
                .claim(MEMBER_ID, memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(authTokenContext.getSecretKey()).compact();
    }
}
