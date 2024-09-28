package org.c4marathon.assignment.product.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.product.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.c4marathon.assignment.global.utils.PageUtil.SMALL_PAGE_SIZE;
import static org.c4marathon.assignment.product.domain.QProduct.product;
import static org.c4marathon.assignment.transaction.domain.QTransaction.transaction;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProductDto> getProducts(Long productCursorId, String searchKeyword) {
        BooleanBuilder dynamicLtProductId = new BooleanBuilder();

        if (productCursorId != null) {
            dynamicLtProductId.and(transaction.id.lt(productCursorId));
        }

        return queryFactory
                .select(Projections.constructor(ProductDto.class,
                        product.id,
                        product.productName,
                        product.price,
                        product.description))
                .from(product)
                .where(dynamicLtProductId
                        .and(product.productName.like("searchKeyword%")))
                .orderBy(product.id.desc(), product.productName.asc())
                .limit(SMALL_PAGE_SIZE + 1)
                .fetch();
    }
}
