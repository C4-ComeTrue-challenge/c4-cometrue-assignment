package org.c4marathon.assignment.product.service;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.PRODUCT_NOT_FOUND;

import org.c4marathon.assignment.global.exception.ProductException;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final ProductRepository productRepository;

    public Product findProductBy(Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
    }

    @Transactional
    public void decreaseProductStock(Long productId, Long quantity) {
        Product product = findProductBy(productId);
        product.decreaseStock(quantity);
    }
}
