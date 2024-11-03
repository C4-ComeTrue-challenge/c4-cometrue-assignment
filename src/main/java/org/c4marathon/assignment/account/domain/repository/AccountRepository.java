package org.c4marathon.assignment.account.domain.repository;

import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account> findAccountByIdWithPessimisticLock(@Param("id") Long id);
    Optional<Account> findAccountByAuthorityAndMemberAuthId(MemberAuthority authority, Long memberAuthId);
}
