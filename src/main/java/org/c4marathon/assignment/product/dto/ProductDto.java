package org.c4marathon.assignment.product.dto;

public record ProductDto(
        Long productId,
        String productName,
        Long price,
        String description
) {
}
