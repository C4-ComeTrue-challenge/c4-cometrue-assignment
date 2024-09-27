package org.c4marathon.assignment.auth.domain.repository;

import org.c4marathon.assignment.auth.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByMemberId(Long memberId);
}