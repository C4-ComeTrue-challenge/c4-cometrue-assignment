package org.c4marathon.assignment.account.dto.response;

import org.c4marathon.assignment.account.domain.Balance;
import org.c4marathon.assignment.transaction.dto.TransactionDto;

import java.util.List;

public record AccountDto(
        String nickname,
        Long balance,
        List<TransactionDto> transactions
) {
}
