package org.c4marathon.assignment.product.dto.request;

public record CreateProductRequest(
    String productName,
    String description,
    Long price,
    Long stock
) {
}
