package org.c4marathon.assignment.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.member.domain.Merchant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "merchant_id", "productName"})
        })
public class Product {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "merchant_id")
    @JsonIgnore
    private Merchant merchant;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Getter
    @Column(nullable = false)
    private Long price;

    @Embedded
    private Stock stock;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Product(final Merchant merchant,
                    final String productName,
                    final String description,
                    final Long price,
                    final Stock stock
    ) {
        this.merchant = merchant;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public static Product of(final Merchant merchant,
                             final String productName,
                             final String description,
                             final Long price,
                             final Stock stock
    ) {
        return new Product(merchant, productName, description, price, stock);
    }

    public void decreaseStock(final Long quantity) {
        stock.decreaseStock(quantity);
    }
}
