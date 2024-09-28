package org.c4marathon.assignment.account.dto;

import org.c4marathon.assignment.account.domain.Balance;

import java.util.List;

public record AccountDto(
        String nickname,
        Balance balance,
        List<TransactionDto> transactions
) {
}
