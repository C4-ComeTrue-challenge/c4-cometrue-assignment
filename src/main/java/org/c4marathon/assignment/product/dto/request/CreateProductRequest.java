package org.c4marathon.assignment.product.dto.request;

public record CreateProductRequest(
    String productName,
    Long price,
    String description
) {
}
