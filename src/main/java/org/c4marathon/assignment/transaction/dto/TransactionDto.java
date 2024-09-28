package org.c4marathon.assignment.transaction.dto;

import java.time.LocalDateTime;

public record TransactionDto(
        Long transactionId,
        String fromNickname,
        String toNickname,
        Long money,
        LocalDateTime transactionDate
) {
}
