package org.c4marathon.assignment.product.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(Merchant merchant,
                           String productName,
                           Long price,
                           String description) {
        productRepository.save(Product.of(merchant, productName, price, description));
    }
}
