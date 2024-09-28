package org.c4marathon.assignment.account.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "authority", "member_auth_id"})
        })
public class Account {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Embedded
    private Balance balance;

    @Column(nullable = false, length = 10)
    private MemberAuthority authority;

    @Column(nullable = false)
    private Long memberAuthId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Account(Balance balance, MemberAuthority authority, Long memberAuthId) {
        this.balance = balance;
        this.authority = authority;
        this.memberAuthId = memberAuthId;
    }

    public static Account of(Balance balance, MemberAuthority authority, Long memberAuthId) {
        return new Account(balance, authority, memberAuthId);
    }
}
