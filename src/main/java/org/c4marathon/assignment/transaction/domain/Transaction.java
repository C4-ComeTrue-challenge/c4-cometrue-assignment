package org.c4marathon.assignment.transaction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.account.domain.Account;
import org.c4marathon.assignment.account.domain.Balance;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false, length = 20)
    private String fromNickname;

    @Column(nullable = false)
    private Long toAccountId;

    @Column(nullable = false, length = 20)
    private String toNickname;

    @Column(nullable = false)
    private Long money;

    @Column(nullable = false)
    private Balance balance;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime transactionDate;

    private Transaction(
            final Account account,
            final Long fromAccountId, final String fromNickname,
            final Long toAccountId, final String toNickname,
            final Long money, final Balance balance) {
        this.account = account;
        this.fromAccountId = fromAccountId;
        this.fromNickname = fromNickname;
        this.toAccountId = toAccountId;
        this.toNickname = toNickname;
        this.money = money;
        this.balance = balance;
    }

    public static Transaction of(
            Account account,
            Long fromAccountId, String fromNickname,
            Long toAccountId, String toNickname,
            Long money, Balance balance) {
        return new Transaction(account, fromAccountId, fromNickname, toAccountId, toNickname, money, balance);
    }
}