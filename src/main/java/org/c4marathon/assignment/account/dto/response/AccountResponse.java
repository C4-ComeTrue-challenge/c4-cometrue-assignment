package org.c4marathon.assignment.account.dto.response;

import java.util.List;

import org.c4marathon.assignment.transaction.dto.TransactionDto;

public record AccountResponse(
        Boolean hasNext,
        Integer size,
        Long transactionCursorId,
        String nickname,
        Long balance,
        List<TransactionDto> transactions
) {
}
