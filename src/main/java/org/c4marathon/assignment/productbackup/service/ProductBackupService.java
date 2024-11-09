package org.c4marathon.assignment.productbackup.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.productbackup.domain.ProductBackup;
import org.c4marathon.assignment.productbackup.domain.repository.ProductBackupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductBackupService {

    private final ProductBackupRepository productBackupRepository;

    @Transactional
    public void save(ProductBackup productBackup) {
        productBackupRepository.save(productBackup);
    }
}
