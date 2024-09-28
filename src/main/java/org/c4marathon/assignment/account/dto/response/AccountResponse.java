package org.c4marathon.assignment.account.dto.response;

import org.c4marathon.assignment.transaction.dto.TransactionDto;

import java.util.List;

public record AccountResponse(
        Boolean hasNext,
        Integer size,
        Long transactionCursorId,
        String nickname,
        Long balance,
        List<TransactionDto> transactions
) {
}
