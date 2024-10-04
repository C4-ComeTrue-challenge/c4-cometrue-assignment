package org.c4marathon.assignment.product.service;

import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;

import java.util.List;

import org.c4marathon.assignment.product.domain.repository.ProductQueryRepository;
import org.c4marathon.assignment.product.dto.ProductDto;
import org.c4marathon.assignment.product.dto.ProductPageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProductService {

    private final ProductQueryRepository productQueryRepository;

    @Transactional(readOnly = true)
    public ProductPageDto getProducts(Long productCursorId, String searchKeyword) {
        List<ProductDto> products = productQueryRepository.getProducts(productCursorId, searchKeyword);
        Boolean hasNext = products.size() > SMALL_PAGE_SIZE;
        Integer size = hasNext ? SMALL_PAGE_SIZE : products.size();
        Long productCursor = hasNext ? products.get(SMALL_PAGE_SIZE - 1).productId() : null;

        return new ProductPageDto(searchKeyword, size, hasNext, productCursor, products);
    }
}
