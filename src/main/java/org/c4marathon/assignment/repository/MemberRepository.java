package org.c4marathon.assignment.repository;

import org.c4marathon.assignment.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByNickname(String email);
}
