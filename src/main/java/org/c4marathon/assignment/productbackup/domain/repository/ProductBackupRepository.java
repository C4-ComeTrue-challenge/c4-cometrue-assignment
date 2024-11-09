package org.c4marathon.assignment.productbackup.domain.repository;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.productbackup.domain.ProductBackup;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductBackupRepository {

    private final ProductBackupJpaRepository productBackupJpaRepository;

    public void save(ProductBackup productBackup) {
        productBackupJpaRepository.save(productBackup);
    }
}
