package org.c4marathon.assignment.account.domain.repository;

import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAuthorityAndMemberAuthId(MemberAuthority authority, Long memberAuthId);
}
