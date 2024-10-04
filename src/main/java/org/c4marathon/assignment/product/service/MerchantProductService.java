package org.c4marathon.assignment.product.service;

import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.Stock;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MerchantProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void addProduct(Merchant merchant,
                           String productName,
                           String description,
                           Long price,
                           Long stock
    ) {
        productRepository.save(Product.of(merchant, productName, description, price, new Stock(stock)));
    }
}
