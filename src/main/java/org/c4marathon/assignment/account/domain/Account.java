package org.c4marathon.assignment.account.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.c4marathon.assignment.member.domain.MemberAuthority;
import org.c4marathon.assignment.transaction.domain.Transaction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @Column(nullable = false, length = 20)
    private String nickname;

    @Getter
    @Embedded
    private Balance balance;

    @Column(nullable = false, length = 10)
    @Enumerated(STRING)
    private MemberAuthority authority;

    @Column(name = "member_auth_id", nullable = false)
    private Long memberAuthId;

    @OneToMany(mappedBy = "account", fetch = LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Account(final String nickname,
                    final Balance balance,
                    final MemberAuthority authority,
                    final Long memberAuthId) {
        this.nickname = nickname;
        this.balance = balance;
        this.authority = authority;
        this.memberAuthId = memberAuthId;
    }

    public static Account of(String nickname, Balance balance, MemberAuthority authority, Long memberAuthId) {
        return new Account(nickname, balance, authority, memberAuthId);
    }

}
