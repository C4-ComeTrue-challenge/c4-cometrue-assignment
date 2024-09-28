package org.c4marathon.assignment.account.dto;

import java.time.LocalDateTime;

public record TransactionDto(
        String fromNickname,
        String toNickname,
        Long money,
        LocalDateTime transactionDate
) {
}
