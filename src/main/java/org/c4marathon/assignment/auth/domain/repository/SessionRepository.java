package org.c4marathon.assignment.auth.domain.repository;

import org.c4marathon.assignment.auth.domain.Session;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByAuthorityAndMemberAuthId(MemberAuthority authority, Long memberAuthId);
}
