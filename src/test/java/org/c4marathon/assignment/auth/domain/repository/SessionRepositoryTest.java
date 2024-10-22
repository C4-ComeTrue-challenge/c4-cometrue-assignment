package org.c4marathon.assignment.auth.domain.repository;

import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;

import org.c4marathon.assignment.auth.domain.Session;
import org.c4marathon.assignment.member.domain.Member;
import org.c4marathon.assignment.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SessionRepositoryTest {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("회원ID를 통해서 Session 정보를 조회해올 수 있다.")
    @Test
    void findSessionByMemberId() {
        // given
        Member member = Member.customer("tester", "test");
        memberRepository.save(member);
        Session session = sessionRepository.save(Session.of("eeee", CUSTOMER, member.getCustomerId()));
        sessionRepository.save(session);
        // when
        Session findSession = sessionRepository.findByAuthorityAndMemberAuthId(CUSTOMER, member.getCustomerId());
        // then
        Assertions.assertEquals(session, findSession);
    }
}
