package org.c4marathon.assignment.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.member.domain.Merchant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    @JsonIgnore
    private Merchant merchant;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable =false)
    private Long stock;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Product(final Merchant merchant,
                    final String productName,
                    final String description,
                    final Long price,
                    final Long stock
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
                             final Long stock
    ) {
        return new Product(merchant, productName, description, price, stock);
    }
}
