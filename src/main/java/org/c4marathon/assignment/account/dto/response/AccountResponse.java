package org.c4marathon.assignment.account.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import org.c4marathon.assignment.transaction.dto.TransactionDto;

@Builder
public record AccountResponse(
        Boolean hasNext,
        Integer size,
        LocalDateTime transactionDateCursor,
        String nickname,
        Long balance,
        List<TransactionDto> transactions
) {
}
