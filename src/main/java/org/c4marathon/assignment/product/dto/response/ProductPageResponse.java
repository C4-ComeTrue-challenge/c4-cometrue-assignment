package org.c4marathon.assignment.product.dto.response;

import org.c4marathon.assignment.product.dto.ProductDto;

import java.util.List;

public record ProductPageResponse(
        String searchKeyword,
        Integer size,
        Boolean hasNext,
        Long productCursorId,
        List<ProductDto> products
) {
}
