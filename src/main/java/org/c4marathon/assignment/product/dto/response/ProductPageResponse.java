package org.c4marathon.assignment.product.dto.response;

import org.c4marathon.assignment.product.dto.ProductDto;
import org.c4marathon.assignment.product.dto.ProductPageDto;

import java.util.List;

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
