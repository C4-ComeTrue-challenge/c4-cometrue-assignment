package org.c4marathon.assignment.product.domain.repository;

import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.merchant.id = :merchantId and p.productName = :productName")
    Optional<Product> findByMerchantIdAndProductName(@Param("merchantId") Long merchantId,
                                                     @Param("productName") String productName);
}
