package org.c4marathon.assignment.member.domain.repository;

import java.util.Optional;

import org.c4marathon.assignment.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
}
