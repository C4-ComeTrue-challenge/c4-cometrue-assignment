package org.c4marathon.assignment.user.domain.repository;

import org.c4marathon.assignment.user.domain.Member;
import org.c4marathon.assignment.user.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
