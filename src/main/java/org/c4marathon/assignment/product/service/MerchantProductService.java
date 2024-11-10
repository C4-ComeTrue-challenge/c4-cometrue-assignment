package org.c4marathon.assignment.product.service;

import org.c4marathon.assignment.global.exception.ProductException;
import org.c4marathon.assignment.member.domain.Merchant;
import org.c4marathon.assignment.member.domain.repository.MerchantRepository;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.Stock;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantProductService {

    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    @Transactional
    public void addProduct(Long merchantId,
                           String productName,
                           String description,
                           Long price,
                           Long stock
    ) {
        Merchant merchant = merchantRepository.findById(merchantId)
                                            .orElseThrow(() -> new ProductException(MEMBER_NOT_FOUND));

        Product product = Product.builder()
                                .merchant(merchant)
                                .productName(productName)
                                .description(description)
                                .price(price)
                                .stock(new Stock(stock))
                                .build();
        productRepository.save(product);
    }
}
