package org.c4marathon.assignment.transaction.dto;

public record ChargeRequest(
        Long customerAccountId,
        Long money
) {
}
