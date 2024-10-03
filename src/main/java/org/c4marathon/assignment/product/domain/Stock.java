package org.c4marathon.assignment.product.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.global.exception.ProductException;

import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.STOCK_CANNOT_NEGATIVE;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Stock {

    @Getter
    private Long stock;

    public Stock(Long stock) {
        if (stock < 0) {
            throw new ProductException(STOCK_CANNOT_NEGATIVE);
        }
        this.stock = stock;
    }

    public void decreaseStock(Long quantity) {
        if (stock < quantity) {
            throw new ProductException(STOCK_CANNOT_NEGATIVE);
        }
        stock -= quantity;
    }
}
