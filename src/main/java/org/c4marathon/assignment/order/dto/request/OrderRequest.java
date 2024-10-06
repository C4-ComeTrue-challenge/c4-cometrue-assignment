package org.c4marathon.assignment.order.dto.request;

public record OrderRequest(
        Long merchantId,
        Long productId,
        Long quantity
) {
}
