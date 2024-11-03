package org.c4marathon.assignment.transaction.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.c4marathon.assignment.account.domain.Balance;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long fromAccountId;

    @NotNull
    @Column(nullable = false, length = 20)
    private String fromNickname;

    @NotNull
    @Column(nullable = false)
    private Long toAccountId;

    @NotNull
    @Column(nullable = false, length = 20)
    private String toNickname;

    @NotNull
    @Column(nullable = false)
    private Long amount;

    @NotNull
    @Column(nullable = false)
    private Long balance;

    @Size(max = 50)
    @Column(name = "memo", length = 50)
    private String memo;

    @NotNull
    @CreatedDate
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;

    @Builder
    private Transaction(
            final Long fromAccountId,
            final String fromNickname,
            final Long toAccountId,
            final String toNickname,
            final Long amount,
            final Long balance,
            final String memo) {
        this.fromAccountId = fromAccountId;
        this.fromNickname = fromNickname;
        this.toAccountId = toAccountId;
        this.toNickname = toNickname;
        this.amount = amount;
        this.balance = balance;
        this.memo = memo;
    }

}
