package org.c4marathon.assignment.product.dto.response;

import java.util.List;

import org.c4marathon.assignment.product.dto.ProductDto;
import org.c4marathon.assignment.product.dto.ProductPageDto;

public record ProductPageResponse(
        String searchKeyword,
        Integer size,
        Boolean hasNext,
        Long productCursorId,
        List<ProductDto> products
) {
    public ProductPageResponse(ProductPageDto dto) {
        this(dto.searchKeyword(),
                dto.size(),
                dto.hasNext(),
                dto.productCursorId(),
                dto.products());
    }
}
