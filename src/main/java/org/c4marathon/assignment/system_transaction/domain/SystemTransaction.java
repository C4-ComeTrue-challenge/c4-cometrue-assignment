package org.c4marathon.assignment.system_transaction.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.system_transaction.domain.TransactionStatus.COMPLETED;
import static org.c4marathon.assignment.system_transaction.domain.TransactionStatus.UNCOMPLETED;

/**
 * 시스템 이익 집계 및 판매자 대금 입금용(확인용)
 * 1. 사용자 포인트 충전 -> 포인트 방식
 * from의 대상 : System(0L)
 * to의 대상 : Customer
 *
 * 2. 판매자 대금 입금 -> 스케줄링
 * from의 대상 : Customer
 * to의 대상 : Merchant
 */
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SystemTransaction {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "system_transcation_id")
    private Long id;

    @Column(name = "from_account_id", nullable = false)
    private Long fromAccountId;

    @Column(name = "to_account_id", nullable = false)
    private Long toAccountId;

    @Enumerated(STRING)
    @Column(name = "transaction_status", nullable = false, length = 10)
    private TransactionStatus status;

    @NotNull
    @CreatedDate
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;

    private SystemTransaction(Long fromAccountId, Long toAccountId, TransactionStatus status) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.status = status;
    }

    public static SystemTransaction charge(Long toAccountId) {
        return new SystemTransaction(0L, toAccountId, COMPLETED);
    }

    public static SystemTransaction billing(Long fromAccountId, Long toAccountId) {
        return new SystemTransaction(fromAccountId, toAccountId, UNCOMPLETED);
    }
}
