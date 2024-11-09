package org.c4marathon.assignment.productbackup.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.productbackup.domain.ProductBackupStatus.UNCOMPLETED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ProductBackup {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "product_backup_id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_change", nullable = false)
    private Long productChange;

    @Column(name = "status", nullable = false)
    private ProductBackupStatus productBackupStatus;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private ProductBackup(Long productId, Long customerId, Long productChange) {
        this.productId = productId;
        this.customerId = customerId;
        this.productChange = productChange;
        this.productBackupStatus = UNCOMPLETED;
    }

    public static ProductBackup of(Long productId, Long customerId, Long productChange) {
        return new ProductBackup(productId, customerId, productChange);
    }
}
