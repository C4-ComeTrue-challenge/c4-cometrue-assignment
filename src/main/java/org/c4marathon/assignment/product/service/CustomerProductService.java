package org.c4marathon.assignment.product.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.product.domain.repository.ProductQueryRepository;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.c4marathon.assignment.product.dto.ProductDto;
import org.c4marathon.assignment.product.dto.response.ProductPageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class CustomerProductService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;

    @Transactional(readOnly = true)
    public ProductPageResponse getProducts(Long productCursorId, String searchKeyword) {
        List<ProductDto> products = productQueryRepository.getProducts(productCursorId, searchKeyword);
        Boolean hasNext = products.size() > SMALL_PAGE_SIZE;
        Integer size = hasNext ? SMALL_PAGE_SIZE : products.size();
        Long productCursor = hasNext ? products.get(SMALL_PAGE_SIZE - 1).productId() : null;

        return new ProductPageResponse(searchKeyword, size, hasNext, productCursor, products);
    }
}
