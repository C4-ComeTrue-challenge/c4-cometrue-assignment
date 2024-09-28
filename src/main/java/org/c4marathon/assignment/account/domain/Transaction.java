package org.c4marathon.assignment.account.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Transaction(
            Long fromAccountId, String fromNickname,
            Long toAccountId, String toNickname,
            Long money) {

        this.fromAccountId = fromAccountId;
        this.fromNickname = fromNickname;
        this.toAccountId = toAccountId;
        this.toNickname = toNickname;
        this.money = money;
    }

    public static Transaction of(
            Long fromAccountId, String fromNickname,
            Long toAccountId, String toNickname,
            Long money) {
        return new Transaction(fromAccountId, fromNickname, toAccountId, toNickname, money);
    }
}