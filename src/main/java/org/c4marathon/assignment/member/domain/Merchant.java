package org.c4marathon.assignment.member.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Merchant {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "merchant_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @OneToMany(mappedBy = "merchant", fetch = LAZY)
    private List<Product> products = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Merchant(final Long memberId, final String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public static Merchant of(Long memberId, String nickname) {
        return new Merchant(memberId, nickname);
    }
}
