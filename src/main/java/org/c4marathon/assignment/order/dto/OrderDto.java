package org.c4marathon.assignment.order.dto;

public record OrderDto(
        Long customerId,
        Long merchantId,
        Long productId,
        Long quantity
) {
}
