package org.c4marathon.assignment.product.dto;

import java.util.List;

public record ProductPageDto(
        String searchKeyword,
        Integer size,
        Boolean hasNext,
        Long productCursorId,
        List<ProductDto> products
) {
}
